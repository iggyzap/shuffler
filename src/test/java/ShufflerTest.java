import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by ignat on 22/03/15.
 */
public class ShufflerTest {

    private List<File> toCleanUp = new ArrayList<File>();

    @Before
    public void setUp() throws Exception {
        File tmpRoot = new File(new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID().toString());
        if (!tmpRoot.mkdirs()) {
            throw new RuntimeException(String.format("Failed to create tmp dir %1$s", tmpRoot));
        }
        toCleanUp.add(tmpRoot);

        FileUtils.copyDirectory(new File("/Users/ignat/private/untitled/src/test/resources"), tmpRoot);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(toCleanUp.get(0));
    }

    @Test
    public void testRenameNoOp() throws Exception {
        Shuffler.main("x", toCleanUp.get(0).getAbsolutePath());
        assertEquals("2 files", 2, toCleanUp.get(0).listFiles().length);
    }

    @Test
    public void testRenameRecursiveAll() throws Exception {
        Shuffler.main("--dryRun=false", toCleanUp.get(0).getAbsolutePath());
        assertEquals("4 files", 4, FileUtils.listFilesAndDirs(toCleanUp.get(0), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).size());
    }

    @Test
    public void testRenameRootHas2Files() throws Exception {
        Shuffler.main("--dryRun=false", toCleanUp.get(0).getAbsolutePath());
        assertEquals("2 files", 2, FileUtils.listFiles(toCleanUp.get(0), null, false).size());
    }


}
