package edu.handong.csee.java.ISEL;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Main {

	public static void main(String args[]) {
		try {

			File directory = new File("/Users/imseongbin/documents/Java/ThreadExamples");
			Git git = Git.init().setDirectory(directory).call();
			// Git git =
			// Git.init().setDirectory("/Users/imseongbin/documents/Java/ThreadExamples").call();

			Repository repository = git.getRepository();

			final String[] list = new File(".").list();
			if (list == null) {
				throw new IllegalStateException("Did not find any files at " + new File(".").getAbsolutePath());
			}
			for (String file : list) {
				if (new File(file).isDirectory()) {
					continue;
				}
				System.out.println("Blaming " + file);
				final BlameResult result = new Git(repository).blame().setFilePath(file).call();
				final RawText rawText = result.getResultContents();
				for (int i = 0; i < rawText.size(); i++) {
					final PersonIdent sourceAuthor = result.getSourceAuthor(i);
					final RevCommit sourceCommit = result.getSourceCommit(i);
					System.out.println(sourceAuthor.getName()
							+ (sourceCommit != null ? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName()
									: "")
							+ ": " + rawText.getString(i));
				}
			}

		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
}
