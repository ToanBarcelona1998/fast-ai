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
data class ImageTaskRequest(
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("taskType") val taskType: String = ETaskType.INFERENCE.type,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
    val checkNsfw: Boolean? = null,
    val positivePrompt: String,
    val negativePrompt: String?,
    val seedImage: String? = null,
    val maskImage: String? = null,
    val strength: Int? = null,
    val width: Int,
    val height: Int,
    val model: String,
    val steps: Int? = null,
    val scheduler: String? = null,
    val seed: Double? = null,
    val CFGScale: Int = 7,
    val clipSkip: Int = 0,
    val usePromptWeighting: Boolean = false,
    val numberResults: Int = 1,
    val customTaskUUID: String? = null,
    val controlNet: List<ControlNetGeneralTaskRequest>? = null,
    val lora: List<LoraTaskRequest>? = null
)

@Serializable
data class ControlNetGeneralTaskRequest(
    val model: String,
    val guideImage: String,
    val weight: Int? = null,
    val startStep: Int? = null,
    val startStepPercentage: Int? = null,
    val endStep: Int? = null,
    val endStepPercentage: Int? = null,
    val controlMode: String = EControlMode.BALANCED.mode,
)

@Serializable
data class LoraTaskRequest(val model: String, val weight: Int)

@Serializable
data class ImageToTextTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.IMAGE_TO_TEXT.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
)

@Serializable
data class RemoveImageBackgroundTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.REMOVE_BG.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
    val postProcessMask: Boolean = false,
    val returnOnlyMask: Boolean = false,
    val alphaMatting: Boolean = false,
    val alphaMattingForegroundThreshold: Int = 10,
    val alphaMattingBackgroundThreshold: Int = 10,
    val alphaMattingErodeSize: Int = 10,
    val rgba: List<Int>? = null,
)

@Serializable
data class UpScaleGanTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.UPSCALE.type,
    @SerialName("inputImage") val inputImage: String,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
    val upscaleFactor: Int
)

@Serializable
data class EnhancePromptTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.PROMPT_ENHANCE.type,
    @SerialName("taskUUID") val taskUUID: String,
    val prompt: String,
    val promptMaxLength: Int?
)