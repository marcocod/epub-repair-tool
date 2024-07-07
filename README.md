# Introduction
I noticed that ePub with DRM protection sometimes contains invalid filename fields in one or more entries.
In these cases the decrypting tool INEPT gives error when it tries to decode the string of the filename.

![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/inept.png?raw=true)

If you open the ePub file with a archive manager program as 7zip you can see that some filenames contain invalid characters.

![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/7zip.png?raw=true)

Note that since the filename contains the entire relative path, archive manager programs can show the corrupted entry in the wrong subdirectory.

# ePub format
The ePub file is a zip archive and in this format the information of each entry is contained in two different headers:
- Local File Header (LFH)
- Central Directory File Header (CDFH)

Usually the filename field in both these headers (CDFH and LFH) should be equal, but sometimes ePub with DRM protection contains few entries where the filename in the CDFH is corrupted and differs from the filename contained in the LFH.

# How the tool works
ePub repair tool analyzes the ePub files contained in the selected folder and detects the files with entries where filename fields of LFH and CDFH are not consistent.
These files are shown in the list on the left (files in read only mode will be skipped).

![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/jar.png?raw=true)

For each file you can check in the table on the right the invalid entries detected by the tool.

Clicking on button "Repair files" the program overwrites the invalid filename field of CDFH with the correct filename field of LFH.
Note that this tool doesn't create any backup file.

# How to build
The repository contains:
- the java source code in the src folder
- the NetBeans project files in the nbproject folder

The code can be built with NetBeans version 8.2 or above opening the repository folder as project folder.
The build requires a Java Development Kit (JDK) version 8 or above.

# How to launch
The release is a Java Archive (JAR) that can be executed with a Java Runtime Environment (JRE) version 8 or above with the following command:

java -jar epub-repair-tool.jar
