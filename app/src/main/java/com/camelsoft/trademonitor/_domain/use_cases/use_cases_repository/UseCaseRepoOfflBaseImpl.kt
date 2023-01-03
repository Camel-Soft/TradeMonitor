package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.net.api.retro.NetApiOfflBase
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import com.camelsoft.trademonitor._presentation.utils.addSep
import com.camelsoft.trademonitor._presentation.utils.getParentFromAbsolute
import com.camelsoft.trademonitor._presentation.utils.isContentTypeExists
import com.camelsoft.trademonitor._presentation.utils.isStatusExists
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import java.util.zip.ZipInputStream
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException
import kotlin.math.roundToInt

class UseCaseRepoOfflBaseImpl(private val netApiOfflBase: NetApiOfflBase): IOfflBase {

    override suspend fun getOfflBase(): Flow<EventsProgress<File>> = flow {
        try {
            val taskFolder = File(getAppContext().externalCacheDir, File.separator+"offlBase")
            val taskFolderTmp = File(taskFolder, File.separator+"offlBaseTmp")
            val taskFile = File(taskFolderTmp, File.separator+"offlbase.zip")

            if (!taskFolderTmp.exists()) {
                if (!taskFolderTmp.mkdirs()) {
                    emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_folder_create)} - ${taskFolderTmp.absolutePath}"))
                    return@flow
                }
            }
            if (taskFile.exists()) {
                if (!taskFile.delete()) {
                    emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_file_delete)} - ${taskFile.absolutePath}"))
                    return@flow
                }
            }

            val buffer = ByteArray(8 * 1024)
            var read: Int
            val fileOutputStream = FileOutputStream(taskFile)
            val response = netApiOfflBase.responseOfflBase()
            val responseBody = response.body()
            if (responseBody == null) {
                emit(EventsProgress.Error("${getAppContext().resources.getString(R.string.error_response_body)} (null)"))
                return@flow
            }
            val inputStream = responseBody.byteStream()
            val length = responseBody.contentLength()
            var capacitor = 0L
            try {
                val stage = getAppContext().resources.getString(R.string.stage_copy)
                var percent = 0
                var percentTemp: Int
                emit(EventsProgress.Progress(
                    stage = stage,
                    percent = percent
                ))
                while (inputStream.read(buffer).also { read = it } != -1) {
                    fileOutputStream.write(buffer, 0, read)
                    capacitor += read
                    percentTemp = (capacitor * 100 / length).toInt()
                    if (percentTemp > percent) {
                        percent = percentTemp
                        emit(EventsProgress.Progress(
                            stage = stage,
                            percent = percent
                        ))
                    }
                }
            }
            catch (e: SSLException) {
                e.printStackTrace()
                emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_сonnect)))
                return@flow
            }
            catch (e: TimeoutCancellationException) {
                e.printStackTrace()
                emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_timeout)))
                return@flow
            }
            catch (e: TimeoutException) {
                e.printStackTrace()
                emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_timeout)))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase][while] ${e.localizedMessage}"))
                return@flow
            }
            finally {
                inputStream.close()
                fileOutputStream.flush()
                fileOutputStream.close()
            }

            if (response.code() != 200) {
                emit(EventsProgress.Error("${getAppContext().resources.getString(R.string.error_not_200)} - ${response.code()} ${response.message()}"))
                return@flow
            }

            if (!response.headers().isStatusExists() || !response.headers().isContentTypeExists()) {
                emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_header_subheaders_not_found)))
                return@flow
            }

            when (response.headers()["status"]) {
                "unauthorized" -> {
                    emit(EventsProgress.UnSuccess(getAppContext().resources.getString(R.string.error_unauthorized)))
                    taskFile.delete()
                    return@flow
                }
                "update" -> {
                    emit(EventsProgress.UnSuccess(getAppContext().resources.getString(R.string.database_update)))
                    taskFile.delete()
                    return@flow
                }
                "unsuccess" -> {
                    emit(EventsProgress.UnSuccess(getAppContext().resources.getString(R.string.error_offlbase_not_found)))
                    taskFile.delete()
                    return@flow
                }
                "error" -> {
                    if (!taskFile.exists()) {
                        emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_unknown)))
                        return@flow
                    }
                    val fileReader = FileReader(taskFile)
                    val bufferedReader = BufferedReader(fileReader)
                    val str = bufferedReader.readLine()
                    bufferedReader.close()
                    fileReader.close()
                    emit(EventsProgress.Error(str))
                    taskFile.delete()
                    return@flow
                }
                "success" -> {
                    if (response.headers()["content-type"] != "application/zip") {
                        emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_content_type)))
                        taskFile.delete()
                        return@flow
                    }
                    if (length != capacitor) {
                        emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_file_size)))
                        taskFile.delete()
                        return@flow
                    }
                    emit(EventsProgress.Success(taskFile))
                    return@flow
                }
                null -> {
                    emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_header_parametr)} - status = null"))
                    taskFile.delete()
                    return@flow
                }
                else -> {
                    emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${getAppContext().resources.getString(R.string.error_header_parametr)} - status = ${response.headers()["status"]}"))
                    taskFile.delete()
                    return@flow
                }
            }
        }
        catch (e: ConnectException) {
            e.printStackTrace()
            emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_сonnect)))
        }
        catch (e: SSLPeerUnverifiedException) {
            e.printStackTrace()
            emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_ssl_unverified)))
        }
        catch (e: SSLHandshakeException) {
            e.printStackTrace()
            emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_handshake)))
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_unknown_host)))
        }
        catch (e: InterruptedIOException) {
            e.printStackTrace()
            emit(EventsProgress.Error(getAppContext().resources.getString(R.string.error_interrupted_io)))
        }
        catch (e: Exception) {
            e.printStackTrace()
            emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.getOfflBase] ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO).buffer(capacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun unzipOfflBase(zipFile: File): Flow<EventsProgress<File>> = flow {
        try {
            if (!zipFile.exists() || !zipFile.isFile) {
                emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.unzipOfflBase] ${getAppContext().resources.getString(R.string.error_file_not_found)} - ${zipFile.absolutePath}"))
                return@flow
            }

            val destFolder = zipFile.absolutePath.getParentFromAbsolute()

            // Удаление-очистка всех файлов кроме архива
            File(destFolder).listFiles()?.forEach { file ->
                if (file.name != zipFile.name) file.delete()
            }

            // Подсчет кол-ва файлов в архиве. Отображаю условный прогресс
            val fis = FileInputStream(zipFile)
            val zis = ZipInputStream(fis)
            var entryTotal = 0
            while (zis.nextEntry != null) {
                entryTotal++
                emit(EventsProgress.Progress(
                    stage=getAppContext().resources.getString(R.string.stage_unpack),
                    percent = entryTotal
                ))
            }
            zis.closeEntry()
            zis.close()
            fis.close()

            // Основная распаковка
            val fileInputStream = FileInputStream(zipFile)
            val zipInputStream = ZipInputStream(fileInputStream)
            var read: Int
            var entryCurrent = 0
            val buffer = ByteArray(8 * 1024)
            var zipEntry = zipInputStream.nextEntry
            while (zipEntry != null) {
                val entry = File(destFolder.addSep()+zipEntry.name)
                val fileOutputStream = FileOutputStream(entry)
                while (zipInputStream.read(buffer).also { read = it } > 0) {
                    fileOutputStream.write(buffer, 0, read)
                }
                fileOutputStream.flush()
                fileOutputStream.close()
                entryCurrent++
                emit(EventsProgress.Progress(
                    stage=getAppContext().resources.getString(R.string.stage_unpack),
                    percent = (entryCurrent * 100 / entryTotal).toFloat().roundToInt()
                ))
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.closeEntry()
            zipInputStream.close()
            fileInputStream.close()
            emit(EventsProgress.Success(File(destFolder)))
        }
        catch (e: Exception) {
            e.printStackTrace()
            emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.unzipOfflBase] ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO).buffer(capacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun publishOfflBase(sourceFolder: File): Flow<EventsProgress<File>> = flow {
        try {
            emit(EventsProgress.Progress(stage=getAppContext().resources.getString(R.string.stage_public), percent = 1))

            val publishFolder = sourceFolder.absolutePath.getParentFromAbsolute()

            // Удаление-очистка всех файлов. Папку не трогаем
            File(publishFolder).listFiles()?.forEach { file ->
                if (file.isFile) file.delete()
            }

            // Копирование
            var total = 0
            sourceFolder.listFiles()?.let { total = it.count() }
            var current = 0
            sourceFolder.listFiles()?.forEach { file ->
                if (file.isFile && (
                            file.name.contains(other = ".dbf", ignoreCase = true)
                                    || file.name.contains(other = ".ndx", ignoreCase = true)
                            )
                ) file.copyTo(target = File(publishFolder.addSep(), file.name), overwrite = true)
                current++
                if (total > 0) emit(EventsProgress.Progress(
                    stage=getAppContext().resources.getString(R.string.stage_public),
                    percent = (current * 100 / total).toFloat().roundToInt()
                ))
            }

            emit(EventsProgress.Success(File(publishFolder)))
        }
        catch (e: Exception) {
            e.printStackTrace()
            emit(EventsProgress.Error("[UseCaseRepoOfflBaseImpl.publishOfflBase] ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO).buffer(capacity = 100, onBufferOverflow = BufferOverflow.DROP_OLDEST)
}
