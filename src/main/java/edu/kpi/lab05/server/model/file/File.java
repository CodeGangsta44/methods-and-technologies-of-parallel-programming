package edu.kpi.lab05.server.model.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private String uid;
    private String name;
    private UploadStatus uploadStatus;
}
