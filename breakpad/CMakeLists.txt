# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(BREAKPAD_SRC_ROOT ${CMAKE_CURRENT_SOURCE_DIR}/cpp/breakpad)

# include breakpad source,
file(GLOB BREAKPAD_SOURCES
        ${BREAKPAD_SRC_ROOT}/src/client/linux/crash_generation/crash_generation_client.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/dump_writer_common/thread_info.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/dump_writer_common/ucontext_reader.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/handler/exception_handler.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/handler/minidump_descriptor.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/log/log.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/microdump_writer/microdump_writer.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/minidump_writer/linux_dumper.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/minidump_writer/linux_ptrace_dumper.cc
        ${BREAKPAD_SRC_ROOT}/src/client/linux/minidump_writer/minidump_writer.cc
        ${BREAKPAD_SRC_ROOT}/src/client/minidump_file_writer.cc
        ${BREAKPAD_SRC_ROOT}/src/common/android/breakpad_getcontext.S
        ${BREAKPAD_SRC_ROOT}/src/common/convert_UTF.c
        ${BREAKPAD_SRC_ROOT}/src/common/md5.cc
        ${BREAKPAD_SRC_ROOT}/src/common/string_conversion.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/elfutils.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/file_id.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/guid_creator.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/linux_libc_support.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/memory_mapped_file.cc
        ${BREAKPAD_SRC_ROOT}/src/common/linux/safe_readlink.cc)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             breakpad-lib

             # Sets the library as a shared library.
             STATIC

             # Provides a relative path to your source file(s).
             ${BREAKPAD_SOURCES})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       breakpad-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

# include breakpad header.
include_directories(
        ${BREAKPAD_SRC_ROOT}/src
        ${BREAKPAD_SRC_ROOT}/src/common/android/include)