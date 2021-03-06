/*
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
package pl.hycom.jira.plugins.gitlab.integration.search;

import org.apache.lucene.document.Document;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;

import java.io.IOException;
import java.util.List;

public interface CommitIndex {
    public static final int HITS_PER_PAGE = 100;

    public void indexFile(Commit commit) throws IOException;

    public List<Document> searchCommits(String fieldName, String fieldValue) throws IOException;

    public List<Commit> searchCommitsByIssue(String jiraIssueKey);

    public boolean checkIfCommitIsIndexed(String idValue) throws IOException;

    public void clearIndex() throws IOException;

    void commit() throws IOException;
}
