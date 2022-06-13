package edu.kpi.lab05.server.repository;

import edu.kpi.lab05.server.model.file.File;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FileRepository {

    private final Set<File> files = new HashSet<>();

    public Optional<File> getFileByUid(final String uid) {

        return files.stream()
                .filter(f -> uid.equals(f.getUid()))
                .findAny();
    }

    public synchronized void addFile(final File file) {

        files.add(file);
    }

    public Set<File> getFiles() {

        return files;
    }
}
