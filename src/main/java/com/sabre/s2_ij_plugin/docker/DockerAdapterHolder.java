package com.sabre.s2_ij_plugin.docker;

public class DockerAdapterHolder {

    private static volatile DockerAdapter instance;

    public static DockerAdapter getInstance() {
        var result = instance;
        if(result != null) {
            return result;
        }

        synchronized (DockerAdapter.class) {
            if (instance == null) {
                instance = DockerAdapterFactory.build();
            }
            return instance;
        }
    }
}
