package com.example.domain.models.requests

import com.example.domain.exceptions.FastAiException
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

enum class PreProcessorType(val type: String) {
    CANNY("canny"),
    DEPTH("depth"),
    ML_SD("mlsd"),
    NORMAL_BAE("normalbae"),
    OPENPOSE("openpose"),
    TILE("tile"),
    SEG("seg"),
    LINE_ART("lineart"),
    LINE_ART_ANIME("lineart_anime"),
    SHUFFLE("shuffle"),
    SCRIBBLE("scribble"),
    SOFT_EDGE("softedge");

    companion object {
        fun fromString(type: String): PreProcessorType? = entries.find { it.type == type }
    }
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
    val strength: Float? = null,
    val width: Int,
    val height: Int,
    val model: String,
    val steps: Int = 20,
    val scheduler: String? = null,
    val seed: Double? = null,
    val CFGScale: Float = 7f,
    val clipSkip: Int = 0,
    val usePromptWeighting: Boolean = false,
    val numberResults: Int = 1,
    val customTaskUUID: String? = null,
    val controlNet: List<ControlNetGeneralTaskRequest>? = null,
    val lora: List<LoraTaskRequest>? = null
){
    fun validate() {
        if (CFGScale < 0 || CFGScale > 30) {
            throw FastAiException(
                FastAiException.RUNWATE_CFG_SCALE_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWATE_CFG_SCALE_OUT_OF_RANGE_ERROR_MESSAGE
            )
        }

        if (clipSkip < 0 || clipSkip > 2) {
            throw FastAiException(
                FastAiException.RUNWARE_CLIP_SKIP_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_CLIP_SKIP_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }

        if (width < 512 || width > 2048) {
            throw FastAiException(
                FastAiException.RUNWARE_WIDTH_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_WIDTH_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }

        if (height < 512 || height > 2048) {
            throw FastAiException(
                FastAiException.RUNWARE_HEIGHT_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_HEIGHT_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }

        if (steps < 1 || steps > 100) {
            throw FastAiException(
                FastAiException.RUNWARE_STEPS_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_STEPS_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }
    }
}

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
){
    fun validate() {
        if (upscaleFactor > 4 || upscaleFactor < 2) {
            throw FastAiException(FastAiException.RUNWARE_UP_SCALE_FACTOR_OVER_ERROR_CODE , FastAiException.RUNWARE_UP_SCALE_FACTOR_OVER_ERROR_MESSAGE)
        }
    }
}

@Serializable
data class EnhancePromptTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.PROMPT_ENHANCE.type,
    @SerialName("taskUUID") val taskUUID: String,
    val prompt: String,
    val promptMaxLength: Int = 400,
    val promptVersion: Int = 1
) {
    fun validate() {
        if (prompt.length > 300) {
            throw FastAiException(
                FastAiException.RUNWARE_ENHANCE_PROMPT_OVER_LENGTH_ERROR_CODE,
                FastAiException.RUNWARE_ENHANCE_PROMPT_OVER_LENGTH_ERROR_MESSAGE
            )
        }
    }
}

@Serializable
data class ControlNetPreprocessTaskRequest(
    @SerialName("taskType") val taskType: String = ETaskType.CONTROL_NET.type,
    @SerialName("taskUUID") val taskUUID: String,
    @SerialName("inputImage") val inputImage: String,
    val preProcessorType: String,
    val height: Int,
    val width: Int,
    val lowThresholdCanny: Int = 100,
    val highThresholdCanny: Int = 200,
    val includeHandsAndFaceOpenPose: Boolean = false,
    @SerialName("outputType") val outputType: String = EOutputType.BASE64.type,
    @SerialName("outputFormat") val outputFormat: String = EOutputFormat.PNG.type,
) {
    fun validate() {
        PreProcessorType.fromString(preProcessorType) ?: throw FastAiException(
            FastAiException.RUNWARE_NOT_SUPPORT_PRE_PROCESSOR_TYPE_ERROR_CODE,
            FastAiException.RUNWARE_NOT_SUPPORT_PRE_PROCESSOR_TYPE_ERROR_MESSAGE
        )

        if (width < 512 || width > 2048) {
            throw FastAiException(
                FastAiException.RUNWARE_WIDTH_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_WIDTH_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }

        if (height < 512 || height > 2048) {
            throw FastAiException(
                FastAiException.RUNWARE_HEIGHT_OUT_OF_RANGE_ERROR_CODE,
                FastAiException.RUNWARE_HEIGHT_OUT_OF_RANGE_ERROR_MESSAGE,
            )
        }

    }
}