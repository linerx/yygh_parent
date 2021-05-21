package com.linerx.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linerx.yygh.common.exception.YyghException;
import com.linerx.yygh.common.result.Result;
import com.linerx.yygh.hosp.service.HospitalSetService;
import com.linerx.yygh.model.hosp.HospitalSet;
import com.linerx.yygh.service.util.MD5;
import com.linerx.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    private Result findAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "删除医院设置")
    @DeleteMapping("{id}")
    private Result deleteHosp(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("getPageHospital/{current}/{limit}")
    private Result getPageHospital(@PathVariable Long current,@PathVariable Long limit,@RequestBody HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);
        //构造查询条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper();
        String hosName = hospitalSetQueryVo.getHosname();
        String hosCode = hospitalSetQueryVo.getHoscode();
        if (StringUtils.isNotBlank(hosName)){
            wrapper.like("hosName",hospitalSetQueryVo.getHosname());
        }
        if (StringUtils.isNotBlank(hosCode)){
            wrapper.eq("hosCode",hospitalSetQueryVo.getHoscode());
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,wrapper);
        return Result.ok(hospitalSetPage);
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("addHospitalSet")
    private Result addHospitalSet(@RequestBody HospitalSet hospitalSet){
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+ random.nextInt()));
        boolean save = hospitalSetService.save(hospitalSet);
        if (save){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id查询医院设置")
    @GetMapping("selectHospitalSetById/{id}")
    private Result selectHospitalSetById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "根据id修改医院设置")
    @PutMapping("updateHospitalSet")
    private Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean result = hospitalSetService.updateById(hospitalSet);
        if (result){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id批量删除医院设置")
    @DeleteMapping("deleteHospitalSet")
    private Result deleteHospitalSet(@RequestBody List<Long> ids){
        boolean result = hospitalSetService.removeByIds(ids);
        if (result){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation(value = "医院设置锁定与解锁")
    @PutMapping("lockHospitalSet/{id}/{state}")
    private Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer state){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(state);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation(value = "发送签名秘钥")
    @PutMapping("sendKey/{id}")
    private Result sendKey(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String hosCode = hospitalSet.getHoscode();
        String signKey = hospitalSet.getSignKey();
        //TODO 发送短信
        return Result.ok();
    }
}
