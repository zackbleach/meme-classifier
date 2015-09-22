package com.zackbleach.meme.scraper;

public class ScrapedImage {

    private String name;
    private String sourceUrl;

    public ScrapedImage(String name, String sourceUrl) {
        this.name = name;
        this.sourceUrl = sourceUrl;
    }

    public ScrapedImage() {

    }

    public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((sourceUrl == null) ? 0 : sourceUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScrapedImage other = (ScrapedImage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sourceUrl == null) {
			if (other.sourceUrl != null)
				return false;
		} else if (!sourceUrl.equals(other.sourceUrl))
			return false;
		return true;
	}

}
