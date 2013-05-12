package com.zackbleach.memetable.imagerecognition.test;

import java.io.IOException;

import net.semanticmetadata.lire.ImageSearchHits;

import org.junit.Test;

import com.zackbleach.memetable.imagerecognition.Indexer;
import com.zackbleach.memetable.imagerecognition.Searcher;

public class IndexerTest {

	@Test
	public void indexMemes() throws IOException {
		Indexer.index();
	}
}
