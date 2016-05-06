package pl.hycom.jira.plugins.gitlab.integration.gitpanel.impl;

import pl.hycom.jira.plugins.gitlab.integration.gitpanel.api.ProcessorInterface;

import java.util.List;

/**
 * Created by Thorgal on 08.04.2016.
 */
public class ProcessorManager {
    private List<ProcessorInterface> processorsList;

    public ProcessorManager(List<ProcessorInterface> processorsList){
        this.processorsList = processorsList;
    }

    public void startProcessors(List<Commit> commitInfoList){
        for (Commit commitInfo : commitInfoList){
            for (ProcessorInterface processor : processorsList) {
                processor.execute(commitInfo);
            }
        }
    }
}
