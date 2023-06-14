package com.iknowhow.mhte.projectsexperience.dto;

import lombok.Data;

@Data
public class DownloadFileDTO {

    private String filename;
    private byte[] file;

}
