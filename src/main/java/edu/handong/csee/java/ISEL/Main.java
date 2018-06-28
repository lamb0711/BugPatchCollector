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

			File directory = new File("/Users/imseongbin/documents/Java/BugPatchCollector");
			//File directory = new File("/Users/imseongbin/documents/Java/BugPatchCollector/src/main/java/edu/handong/csee/java/ISEL");
			Git git = Git.open(directory);
			//Git git = Git.init().setDirectory(directory).call();
			// Git.init().setDirectory("/Users/imseongbin/documents/Java/ThreadExamples").call();

			//git.b;
			
			Repository repository = git.getRepository();
			
			File ndirectory = new File("/Users/imseongbin/documents/Java/BugPatchCollector/src/main/java/edu/handong/csee/java/ISEL"); //수정중..
			
			
			
			final String[] list = ndirectory.list();
			
			if (list == null) {
				throw new IllegalStateException("Did not find any files at " + new File(".").getAbsolutePath());
			} 
			for (String file : list) {
				String nfile = "src/main/java/edu/handong/csee/java/ISEL" + "/"+ file;
				
				if (new File(file).isDirectory()) {
					continue;
				}
				System.out.println("Blaming " + nfile);
				BlameResult result = new Git(repository).blame().setFilePath(nfile).call();
				RawText rawText = result.getResultContents();
				for (int i = 0; i < rawText.size(); i++) {
					final PersonIdent sourceAuthor = result.getSourceAuthor(i);
					final RevCommit sourceCommit = result.getSourceCommit(i);
					System.out.println(sourceAuthor.getName()
							+ (sourceCommit != null ? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName()
									: "")
							+ ": " + rawText.getString(i));

				}
			}/**/
			String[] gitignoreList = null; //여기 수정 예정.
			//recur(directory.toString(), repository, gitignoreList);
			
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public static void recur(String directory, Repository repository, String[] gitignoreList) throws GitAPIException {
		
		/*
		System.out.println("우선여기 @@@@@" + directory);
		for (String file : list) {
			System.out.println("**"+file);
		}/**/
		

		String[] list = new File(directory).list();
		if (list == null) {
			throw new IllegalStateException("Did not find any files at " + new File(directory).getAbsolutePath());
		}
		
		
		
		
		/* 여기 for문 안에서 file이 .gitignore파일에 존재하는 파일인지 체크하고 그렇다면 continue해야함
		 * .gitignore에서는 3가지 패턴
		 * (1) *.class	-> first star
		 * (2) example*	-> last star
		 * (3) file.tmp	-> exact file name
		 */
		for (String file : list) {
			
			String nfile = directory + "/" +file.toString();
			
			//System.out.println("File: " + nfile);
			
			if (new File(nfile).isDirectory()) {
				//System.out.println("call recur");
				recur(nfile,repository,gitignoreList);
			}
			
			
			else  if(nfile.contains(".java") && !nfile.contains("Test")){ //여기도 수정 필요.
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
