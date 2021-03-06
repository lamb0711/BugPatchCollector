package edu.handong.csee.isel.patch;

public class CommitStatus {
	public String getProject() {
		return project;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public String getCommitHash() {
		return commitHash;
	}

	@Override
	public String toString() {
		return "CommitStatus [project=" + project + ", shortMessage=" + shortMessage + ", commitHash=" + commitHash
				+ ", date=" + date + ", author=" + author + ", path=" + path + ", patch=" + patch + "]";
	}

	public int getDate() {
		return date;
	}

	public String getAuthor() {
		return author;
	}

	public String getPath() {
		return path;
	}

	public String getPatch() {
		return patch;
	}

	public CommitStatus(String project, String shortMessage, String commitHash, int date, String author, String path,
			String patch) {
		this.project = project;
		this.shortMessage = shortMessage;
		this.commitHash = commitHash;
		this.date = date;
		this.author = author;
		this.path = path;
		this.patch = patch;
	}

	private String project;
	private String shortMessage;
	private String commitHash;
	private int date;
	private String author;
	private String path;
	private String patch;

}