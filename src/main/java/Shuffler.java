import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple renaming utility which recursively renames files from given root to root giving them UUID name and
 * keeping extension
 */
public class Shuffler {
    private static Pattern pattern = Pattern.compile("(.*)(\\..+)");

    private static boolean dryRun = true;

    public static void main(String... args) {
        File root = new File(args[1]);
        if (!root.isDirectory()) {
            System.exit(1);
        }
        if ("--dryRun=false".equals(args[0])) {
            dryRun = false;
        }
        List<File> filesToRename = new ArrayList<File>();

        find(filesToRename, root);
        Collections.shuffle(filesToRename);
        rename(filesToRename, root);
    }

    private static void rename(List<File> filesToRename, File root) {
        for (File current : filesToRename) {
            String name = current.getName();
            Matcher m = pattern.matcher(name);
            if (!m.matches()) {
                throw new IllegalStateException(String.format("File does not match! %1$s", current.getAbsolutePath()));
            }
            String extension = m.group(2);
            File newPath = new File(root, UUID.randomUUID().toString() + extension);
            System.out.println(String.format("%1$s -> %2$s", current.getAbsolutePath(), newPath.getAbsolutePath()));
            if (!dryRun) {
                if (!current.renameTo(newPath)) {
                    System.err.println(String.format("Failed renaming %1$s to %2$s",
                            current.getAbsolutePath(), newPath.getAbsolutePath()));
                }
            }
        }

    }

    private static void find(List<File> filesToRename, File current) {
        if (current.isDirectory()) {
            if (current.isHidden()) {
                //do not process hidden dirs
                return;
            }
            for (File file : current.listFiles()) {
                find(filesToRename, file);
            }
        }

        String name = current.getName();
        Matcher m = pattern.matcher(name);
        if (m.matches()) {
            filesToRename.add(current);
        }
    }


}
