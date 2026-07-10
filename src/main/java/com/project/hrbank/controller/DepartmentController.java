package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.DepartmentControllerDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"/api/departments"})
@RequiredArgsConstructor
@Slf4j
public class DepartmentController implements DepartmentControllerDoc {
}
