package com.skyline.hack.captcha.core.spliter;

import com.skyline.hack.captcha.core.spliter.flooding.FloodingSpliter;

/**
 * 默认的分割器
 * 
 * @author jairus
 *
 */
public class DefaultSpliter extends FloodingSpliter implements Spliter {

	public DefaultSpliter(int destWidth, int destHeight) {
		super(destWidth, destHeight, destHeight);
	}
}
