package aircleanprojectback.restapi.member.controller;


import aircleanprojectback.restapi.common.dto.Criteria;
import aircleanprojectback.restapi.common.dto.PageDTO;
import aircleanprojectback.restapi.common.dto.PagingResponseDTO;
import aircleanprojectback.restapi.common.dto.ResponseDTO;
import aircleanprojectback.restapi.member.dto.*;
import aircleanprojectback.restapi.member.service.BranchResourceService;
import aircleanprojectback.restapi.member.service.EmployeeResourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/members")
public class HumanResourceController {

    private final BranchResourceService branchService;
    private final EmployeeResourceService service;
    private final ObjectMapper objectMapper;

    @Autowired
    public HumanResourceController(EmployeeResourceService service, ObjectMapper objectMapper,
                                   BranchResourceService branchService){
        this.service = service;
        this.objectMapper=objectMapper;
        this.branchService = branchService;
    }
    // 조회
    @GetMapping("/employee")
    public ResponseEntity<ResponseDTO> findEmployee(@RequestParam(defaultValue = "1") String offset){

        System.out.println("findEmployee 동작합니다");

        System.out.println("offset = " + offset);

        Criteria cri = new Criteria(Integer.parseInt(offset),12);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

        Page<MembersAndEmployeeDTO> employeeList = service.getEmployeeListWithPaging(cri);

        System.out.println("employeeList = " + employeeList.getContent());

        pagingResponseDTO.setData(employeeList);


        pagingResponseDTO.setPageInfo(new PageDTO(cri,(int)employeeList.getTotalElements()));


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"직원 조회 성공",pagingResponseDTO));
    }

    @GetMapping("/branch")
    public ResponseEntity<ResponseDTO> findBranch(@RequestParam(defaultValue = "1") String offset){

        Criteria cri = new Criteria(Integer.parseInt(offset),12);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

        Page<MembersAndBranchDTO> branchList = branchService.getBranchListWithPaging(cri);

        System.out.println("branchList = " + branchList.getContent());

        pagingResponseDTO.setData(branchList);


        pagingResponseDTO.setPageInfo(new PageDTO(cri,(int)branchList.getTotalElements()));


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"직원 조회 성공",pagingResponseDTO));
    }

    @GetMapping("/driver")
    public ResponseEntity<ResponseDTO> findDriver(){
        return null;
    }

    // 검색
    @GetMapping("employee/search")
    public ResponseEntity<ResponseDTO> findEmployeeById(@RequestBody SearchDTO searchDTO){

        PagingResponseDTO pagingResponseDTO= new PagingResponseDTO();
        Page<MembersAndEmployeeDTO> employeeList = null;


        switch (searchDTO.getCategory()){
            case "name":

                break;
            case "dept":

                break;
            case "position":

                break;
        }
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"일단 들어옴",searchDTO));
    }

    @GetMapping("branch/{memberId}")
    public ResponseEntity<ResponseDTO> findBranchId(@PathVariable String memberId){
        return null;
    }

    @GetMapping("driver/{memberId}")
    public ResponseEntity<ResponseDTO> findDriverById(@PathVariable String memberId){
        return null;
    }

    // 등록
    @PostMapping("employee")
    public ResponseEntity<ResponseDTO> registEmployee(@ModelAttribute MemberDTO memberDTO, @ModelAttribute EmployeeDTO employeeDTO, MultipartFile image){

        System.out.println("memberDTO = " + memberDTO);
        System.out.println("employeeDTO = " + employeeDTO);

        memberDTO.setMemberStatus("Y");

        service.registEmployee(memberDTO,employeeDTO,image);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"등록 성공",memberDTO));
    }
    @PostMapping("branch")
    public ResponseEntity<ResponseDTO> registBranch(/*@RequestBody*/){
        return null;
    }
    @PostMapping("driver")
    public ResponseEntity<ResponseDTO> registDriver(/*@RequestBody*/){
        return null;
    }

    // 수정
    @PutMapping("employee/{employeeCode}")
    public ResponseEntity<ResponseDTO> modifyEmployee(@PathVariable int employeeCode, @ModelAttribute MemberModifyDTO memberModifyDTO,MultipartFile image){

        System.out.println("employeeCode = " + employeeCode);
        System.out.println("memberModifyDTO = " + memberModifyDTO);

        service.modifyEmployeeInfo(employeeCode,memberModifyDTO,image);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"일단 들어옴",memberModifyDTO));
    }
    @PutMapping("branch/{memberId}")
    public ResponseEntity<ResponseDTO> modifyBranch(/*@RequestBody*/@PathVariable String memberId){
        return null;
    }
    @PutMapping("driver/{memberId}")
    public ResponseEntity<ResponseDTO> modifyDriver(/*@RequestBody*/ @PathVariable String memberId){
        return null;
    }

    // softDelete
    @PutMapping("/employee/soft-delete")
    public ResponseEntity<ResponseDTO> softDeleteEmployee(@RequestBody List<String> deleteMember) {


        for(String m : deleteMember){
            System.out.println("m = " + m);
        }

        service.findEmployeeById(deleteMember,"N");


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"soft delete 수행",deleteMember));
    }

    @PutMapping("branch/soft-delete/")
    public ResponseEntity<ResponseDTO> softDeleteBranch(@PathVariable String[] members){
        return null;
    }
    @PutMapping("driver/soft-delete/")
    public ResponseEntity<ResponseDTO> softDeleteDriver(@PathVariable String[] members){
        return null;
    }


    // 삭제가능 회원 조회
    @GetMapping("employee/unstatus")
    public ResponseEntity<ResponseDTO> findMemberY(@RequestParam(defaultValue = "1")String offset, @RequestParam(defaultValue = "10")String amount){

        System.out.println("offset = " + offset);
        System.out.println("amount = " + amount);


        Criteria cri = new Criteria(Integer.parseInt(offset),Integer.parseInt(amount));

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

        Page<MembersAndEmployeeDTO> employeeList = service.getEmployeeWhereY(cri);

        System.out.println("employeeList = " + employeeList.getContent());

        pagingResponseDTO.setData(employeeList);

        pagingResponseDTO.setPageInfo(new PageDTO(cri,(int)employeeList.getTotalElements()));



        return  ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"일단 들어옴",pagingResponseDTO));
    }

    //회원 삭제
    @DeleteMapping("/employee")
    public ResponseEntity<ResponseDTO> deleteEmployee(@RequestBody List<String> memberId){

        memberId.forEach(System.out::println);

        service.deleteMemberById(memberId);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"삭제 성공","간디"));
    }


    // 회원 롤백
    @PutMapping("employee/status")
    public ResponseEntity<ResponseDTO> employeeRollBack(@RequestBody List<String> memberIds){

        memberIds.forEach(System.out::println);

        service.findEmployeeById(memberIds,"Y");


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"롤백성공",memberIds));
    }



}
