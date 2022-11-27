package com.ukang.baiyu.entity;

public class SearchNews {
	private String pmid;

	private String pubdate;

	private String authorstr;

	private String title;

	private String fulljournalname;

	private String issn;

	private String essn;

	private String nlmid;

	public void setPmid(String pmid) {
		this.pmid = pmid;
	}

	public String getPmid() {
		return this.pmid;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getPubdate() {
		return this.pubdate;
	}

	public void setAuthorstr(String authorstr) {
		this.authorstr = authorstr;
	}

	public String getAuthorstr() {
		return this.authorstr;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setFulljournalname(String fulljournalname) {
		this.fulljournalname = fulljournalname;
	}

	public String getFulljournalname() {
		return this.fulljournalname;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getIssn() {
		return this.issn;
	}

	public void setEssn(String essn) {
		this.essn = essn;
	}

	public String getEssn() {
		return this.essn;
	}

	public void setNlmid(String nlmid) {
		this.nlmid = nlmid;
	}

	public String getNlmid() {
		return this.nlmid;
	}
}
