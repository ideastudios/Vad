# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_definitions(-DWEBRTC_POSIX)
add_definitions(-DWEBRTC_ANDROID)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -pedantic")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        oomvad-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/jni/native-lib.c
        src/main/jni/vad_src/checks.cc
        src/main/jni/vad_src/checks.h
        src/main/jni/vad_src/constructormagic.h
        src/main/jni/vad_src/cpu_features_wrapper.h
        src/main/jni/vad_src/cross_correlation.c
        src/main/jni/vad_src/deprecation.h
        src/main/jni/vad_src/division_operations.c
        src/main/jni/vad_src/downsample_fast.c
        src/main/jni/vad_src/energy.c
        src/main/jni/vad_src/get_scaling_square.c
        src/main/jni/vad_src/min_max_operations.c
        src/main/jni/vad_src/resample_48khz.c
        src/main/jni/vad_src/resample_by_2_internal.c
        src/main/jni/vad_src/resample_by_2_internal.h
        src/main/jni/vad_src/resample_fractional.c
        src/main/jni/vad_src/scoped_ptr.h
        src/main/jni/vad_src/signal_processing_library.h
        src/main/jni/vad_src/spl_init.c
        src/main/jni/vad_src/spl_inl.h
        src/main/jni/vad_src/spl_inl_armv7.h
        src/main/jni/vad_src/template_util.h
        src/main/jni/vad_src/typedefs.h
        src/main/jni/vad_src/vad.cc
        src/main/jni/vad_src/vad.h
        src/main/jni/vad_src/vad_core.c
        src/main/jni/vad_src/vad_core.h
        src/main/jni/vad_src/vad_filterbank.c
        src/main/jni/vad_src/vad_filterbank.h
        src/main/jni/vad_src/vad_gmm.c
        src/main/jni/vad_src/vad_gmm.h
        src/main/jni/vad_src/vad_sp.c
        src/main/jni/vad_src/vad_sp.h
        src/main/jni/vad_src/vector_scaling_operations.c
        src/main/jni/vad_src/webrtc_vad.c
        src/main/jni/vad_src/webrtc_vad.h)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        oomvad-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})