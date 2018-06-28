package edu.handong.csee.java.ISEL;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class Main {

	public static void main(String args[]) {
		try {
			File directory = new File("/Users/imseongbin/documents/Java/BugPatchCollector");
			
			
			Git git = Git.open(directory);
			Repository repository = git.getRepository();
			recur(directory.toString(), repository);
			
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public static void recur(String directory, Repository repository) throws GitAPIException {
		
		String[] list = new File(directory).list();
		for (String file : list) {
			
			String nfile = directory + "/" +file.toString();
			
			if (new File(nfile).isDirectory()) {
				recur(nfile,repository);
			}
			else if(file.contains(".java")){
				String nnfile = nfile.substring(repository.getWorkTree().toString().length()+1);
				
				System.out.println("Blaming " + nnfile);
				final BlameResult result = new Git(repository).blame().setFilePath(nnfile).call();
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
		}

	}
}
