package com.example.domain.models.requests

import kotlinx.serialization.*

enum class ETaskType(val type: String) {
    INFERENCE(type = "imageInference"),
    CONTROL_NET(type = "imageControlNetPreProcess"),
    UPSCALE(type = "imageUpscale"),
    REMOVE_BG(type = "imageBackgroundRemoval"),
    IMAGE_TO_TEXT(type = "imageCaption"),
    PROMPT_ENHANCE(type = "promptEnhance")
}

enum class EOutputType(val type: String) {
    BASE64(type = "base64Data"),
    URI(type = "dataURI"),
    URL(type = "URL")
}

enum class EOutputFormat(val type: String) {
    PJG(type = "PJG"),
    PNG(type = "PNG"),
    WEBP(type = "WEBP")
}

enum class EControlMode(val mode: String) {
    BALANCED("balanced"),
    PROMPT("prompt"),
    CONTROL_NET("controlnet"),
}

@Serializable
open class FastAiRequest(val iTaskType: String, val iTaskUUID: String)

@Serializable
open class FastAIImageRequest(
    val fTaskType: String,
    val fOutputType: String,
    val fOutputFormat: String,
    val fTaskUUID: String,
) : FastAiRequest(iTaskType = fTaskType, iTaskUUID = fTaskUUID)

@Serializable
data class ImageTaskRequest(
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("taskType") val taskType: String = ETaskType.INFERENCE.type,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
    val checkNsfw: Boolean? = null,
    val positivePrompt: String,
    val negativePrompt: String? = null,
    val seedImage: String? = null,
    val maskImage: String? = null,
    val strength: Int? = null,
    val width: Int,
    val height: Int,
    val model: String,
    val steps: Int? = null,
    val scheduler: String? = null,
    val seed: Double? = null,
    val CFGScale: Int? = null,
    val clipSkip: Int? = null,
    val usePromptWeighting: Boolean? = null,
    val numberResults: Int = 1,
    val customTaskUUID: String? = null,
    val controlNet: List<ControlNetGeneralTaskRequest>? = null,
    val lora: List<LoraTaskRequest>? = null
) : FastAIImageRequest(
    fTaskType = taskType,
    fTaskUUID = taskUUID,
    fOutputType = outputType,
    fOutputFormat = outputFormat
)

@Serializable
data class ControlNetGeneralTaskRequest(
    val model: String,
    val guideImage: String,
    val weight: Int?,
    val startStep: Int?,
    val startStepPercentage: Int?,
    val endStep: Int?,
    val endStepPercentage: Int?,
    val controlMode: String = EControlMode.BALANCED.mode,
)

@Serializable
data class LoraTaskRequest(val model: String, val weight: Int)

@Serializable
open class EditImageTaskRequest(
    private val eTaskType: String,
    val eInputImage: String,
    private val eTaskUUID: String,
    private val eOutputType: String,
    private val eOutputFormat: String,
) : FastAIImageRequest(
    fTaskType = eTaskType,
    fTaskUUID = eTaskUUID,
    fOutputFormat = eOutputFormat,
    fOutputType = eOutputType
)

@Serializable
data class ImageToTextTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.IMAGE_TO_TEXT.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
) : EditImageTaskRequest(
    eTaskType = taskType,
    eInputImage = inputImage,
    eTaskUUID = taskUUID,
    eOutputType = outputType,
    eOutputFormat = outputFormat
)

@Serializable
data class RemoveImageBackgroundTaskRequest(
    val postProcessMask: Boolean? = null,
    val returnOnlyMask: Boolean? = null,
    val alphaMatting: Boolean? = null,
    val alphaMattingForegroundThreshold: Int? = null,
    val alphaMattingBackgroundThreshold: Int? = null,
    val alphaMattingErodeSize: Int? = null,
    val rgba: List<Int>? = null,
    @SerialName("taskType") val taskType: String = ETaskType.REMOVE_BG.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
) : EditImageTaskRequest(
    eTaskType = taskType,
    eInputImage = inputImage,
    eTaskUUID = taskUUID,
    eOutputType = outputType,
    eOutputFormat = outputFormat
)

@Serializable
data class UpScaleGanTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.UPSCALE.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
    val upscaleFactor: Double
) : FastAIImageRequest(
    fTaskType = taskType,
    fTaskUUID = taskUUID,
    fOutputType = outputType,
    fOutputFormat = outputFormat
)

@Serializable
data class EnhancePromptTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.PROMPT_ENHANCE.type,
    @SerialName("taskUUID") val taskUUID: String,
    val prompt : String,
    val promptMaxLength: Int?
) :
    FastAiRequest(iTaskType = taskType, iTaskUUID = taskUUID)