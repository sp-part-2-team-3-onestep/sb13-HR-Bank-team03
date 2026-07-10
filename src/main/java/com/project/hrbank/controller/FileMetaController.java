package com.project.hrbank.controller;


import com.project.hrbank.controller.doc.EmployeeControllerDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/files"})
@RequiredArgsConstructor
@Slf4j
public class FileMetaController implements EmployeeControllerDoc {
}
