package pl.hycom.jira.plugins.gitlab.integration.service;

import com.atlassian.jira.issue.Issue;
import lombok.extern.log4j.Log4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.hycom.jira.plugins.gitlab.integration.dao.ConfigManagerDao;
import pl.hycom.jira.plugins.gitlab.integration.dao.ICommitDao;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;
import pl.hycom.jira.plugins.gitlab.integration.search.CommitIndex;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Copyright (c) 2016, Authors</p>
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at</p>
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0</p>
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.</p>
 */
@Service
@Log4j
public class CommitService implements ICommitService {

    @Autowired private ConfigManagerDao dao;
    @Autowired private ICommitDao commitRepository;
    @Autowired private CommitIndex commitSearcher;

    private int perPage = 20;

    @Override
    public List<Commit> getNewCommits(Long projectId) throws SQLException, ParseException, IOException {
        int pageNumber = 1;
        boolean indexedCommitEncountered = false;
        List<Commit> commitsList;
        List<Commit> resultList = new ArrayList<>();

        do {
            commitsList = commitRepository.getNewCommits(dao.getProjectConfig(projectId), perPage, pageNumber);
            for(Commit commit : commitsList) {
                if(!commitSearcher.checkIfCommitIsIndexed(commit.getId())) {
                    resultList.add(commit);
                } else {
                    indexedCommitEncountered = true;
                    break;
                }
            }
            pageNumber++;
        } while(commitsList.size() != perPage && !indexedCommitEncountered);
        return resultList;
    }

    @Override
    public List<Commit> getAllIssueCommits(Issue jiraIssue) throws IOException {
        String issueKey = jiraIssue.getKey();
        return commitSearcher.searchCommitsByIssue(issueKey);
    }

    @Override
    public Commit getOneCommit(Long projectId, String shaSum) throws SQLException {
        return commitRepository.getOneCommit(dao.getProjectConfig(projectId), shaSum);
    }
}
