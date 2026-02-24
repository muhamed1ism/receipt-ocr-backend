package org.ssnardo.receipt_ocr;

import org.springframework.boot.SpringApplication;

public class TestReceiptOcrApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReceiptOcrApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
