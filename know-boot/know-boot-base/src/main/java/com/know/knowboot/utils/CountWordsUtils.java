package com.know.knowboot.utils;

/**
 * 类似Word统计字数
 */
public class CountWordsUtils {
	/**
	 * 统计字数
	 * 
	 * @param context
	 *            文本内容
	 * @return 字数
	 */
	public int getMSWordsCount(String context) {
		int words_count = 0;
		// 中文单词
		String cn_words1 = context.replaceAll("[^(\\u4e00-\\u9fa5，。《》？；’‘：“”【】、）（￥！·)]", "");
		String cn_words = cn_words1.replaceAll("“”", "").replaceAll("“……”", "").replaceAll("\\)", "").replaceAll("\\(", "");
		int cn_words_count = cn_words.length();
		// 非中文单词
		String non_cn_words = context.replaceAll("[^(a-zA-Z0-9`\\-=\';.,/~!@#$%^&*)(_+|}{\":><?\\[\\])]", " ");
		int non_cn_words_count = 0;
		String[] ss = non_cn_words.split(" ");
		for (String s : ss) {
			if (s.trim().length() != 0)
				non_cn_words_count++;
		}
		// 中文和非中文单词合计
		words_count = cn_words_count + non_cn_words_count;
		return words_count;
	}
}
