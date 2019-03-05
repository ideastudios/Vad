# Vad
基于 webrtc 的安卓 vad 库

## 支持的采样率为48000kbps 32000kbps 16000kbps 8000kbps,其他的采样率不支持


## 使用

在录音的读取buffer过程中，调用 vad.processBuffer(sampleRate, buffer, length); 返回值即为当前buffer中是否在说话有人声
buffer大小建议为sampleRate/10

## Gradle
[![](https://jitpack.io/v/ideastudios/Vad.svg)](https://jitpack.io/#ideastudios/Vad)
1. Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the dependency
```
	dependencies {
	       implementation 'com.github.ideastudios:Vad:1.0.1'
	}


```
