package com.wl.util;

import org.eclipse.persistence.internal.jpa.parsing.SubstringNode;

public class Testing {

	public static void main(String[] args) {
		String add="7213044141901111111122222222";
		
		String con=add.substring(0, 12);
		System.out.println(con);
		
		String ref = add.substring(12);
		System.out.println(ref);
	}

}
