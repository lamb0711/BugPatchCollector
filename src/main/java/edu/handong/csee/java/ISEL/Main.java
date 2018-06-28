package edu.handong.csee.java.ISEL;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Main {

	public static void main(String args[]) {
		try {

			//File directory = new File("/Users/imseongbin/documents/Java/ThreadExamples");
			File directory = new File(".");
			Git git = Git.init().setDirectory(directory).call();
			// Git git =
			// Git.init().setDirectory("/Users/imseongbin/documents/Java/ThreadExamples").call();

			Repository repository = git.getRepository();
			
			/*
			final String[] list = directory.list();
			for (String file : list) {
				System.out.println(file.toString());
			}
			
			
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
			}/**/
			recur(directory.toString(), repository);
			
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public static void recur(String directory, Repository repository) throws GitAPIException {
		
		/*
		System.out.println("우선여기 @@@@@" + directory);
		for (String file : list) {
			System.out.println("**"+file);
		}/**/
		

		String[] list = new File(directory).list();
		for (String file : list) {
			
			String nfile = directory + "/" +file.toString();
			
			System.out.println("File: " + nfile);
			
			if (new File(nfile).isDirectory()) {
				System.out.println("call recur");
				recur(nfile,repository);
			}
			/*else {
				System.out.println("file: \""+file+"\"");
			}*/
			
			
			else {
				System.out.println("Blaming " + nfile);
				final BlameResult result = new Git(repository).blame().setFilePath(nfile).call();
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
		/**/
	}
}
