package folksent.model

import folksent.model.entity.BaseAuthor
import folksent.model.entity.BaseTopic
import folksent.model.entity.Document
import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.hasItem
import static org.junit.matchers.JUnitMatchers.hasItems

class InMemoryFolksonomyTest {

    class BaseDocument implements Document {

        def name
        def author = ''
        def topics = [] as Set

        @Override
        String getEntityName() {
            name
        }

        public BaseDocument(name) {
            this.name = name
        }

    }

    class BaseDocumentExtractor implements DocumentInformationExtractor<BaseDocument, BaseAuthor, BaseTopic> {

        @Override
        BaseAuthor extractAuthor(BaseDocument document) {
            new BaseAuthor(document.author)
        }

        @Override
        Collection<BaseTopic> extractTopics(BaseDocument document) {
            document.topics
        }
    }


    def author1 = new BaseAuthor('author1')
    def author2 = new BaseAuthor('author2')

    def doc1 = new BaseDocument('doc1')
    def doc2 = new BaseDocument('doc2')

    def topic1 = new BaseTopic('topic1')
    def topic2 = new BaseTopic('topic2')
    def topic3 = new BaseTopic('topic3')

    def folksonomy = new InMemoryFolksonomy<BaseDocument, BaseAuthor, BaseTopic>(new BaseDocumentExtractor())

    @Before void setUp() {
        doc1.author = author1
        doc1.topics.add topic1
        doc1.topics.add topic2
        folksonomy.addDocument doc1

        doc2.author = author2
        doc2.topics.add topic1
        doc2.topics.add topic3
        folksonomy.addDocument doc2
    }

    @Test void testGetAuthors() {
        def authors =  folksonomy.getAuthors()
        assertThat authors, hasItems(author1, author2)
    }

    @Test void testGetDocumentsByAuthor() {
        assertThat folksonomy.getDocuments(author1), hasItem(doc1)
        assertThat folksonomy.getDocuments(author1), not(hasItem(doc2))
        assertThat folksonomy.getDocuments(author2), hasItem(doc2)
        assertThat folksonomy.getDocuments(author2), not(hasItem(doc1))
    }

    @Test void testGetDocuments() {
        def documents =  folksonomy.getDocuments()
        assertThat documents, hasItems(doc1, doc2)
    }

    @Test void testGetTopicsByDocument() {
        assertThat folksonomy.getTopics(doc1), hasItems(topic1, topic2)
        assertThat folksonomy.getTopics(doc1), not(hasItem(topic3))
        assertThat folksonomy.getTopics(doc2), hasItems(topic1, topic3)
        assertThat folksonomy.getTopics(doc2), not(hasItem(topic2))
    }

    @Test void testGetTopics() {
        assertThat folksonomy.getTopics(), hasItems(topic1, topic2, topic3)
    }

    @Test void testAddDocument() {
        def doc3 = new BaseDocument('doc3')
        doc3.author = author1
        doc3.topics.add topic2
        folksonomy.addDocument doc3

        assertThat folksonomy.getDocuments(), hasItem(doc3)
        assertThat folksonomy.getDocuments(author1), hasItem(doc3)
        assertThat folksonomy.getTopics(doc3), hasItem(topic2)
    }

    @Test void testAsGraph() {
        def graph = FolksonomyUtils.asGraph(folksonomy)

        for (entity in [doc1, doc2, author1, author2, topic1, topic2, topic3]) {
            assertTrue graph.containsVertex(entity)
        }

        assertTrue graph.containsEdge(doc1, author1)
        assertFalse graph.containsEdge(doc1, author2)

        assertTrue graph.containsEdge(doc2, author2)
        assertFalse graph.containsEdge(doc2, author1)

        assertTrue graph.containsEdge(doc1, topic1)
        assertTrue graph.containsEdge(doc1, topic2)
        assertFalse graph.containsEdge(doc1, topic3)
    }
}
