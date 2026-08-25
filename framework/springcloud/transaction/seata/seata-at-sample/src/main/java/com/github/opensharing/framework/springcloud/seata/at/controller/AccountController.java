package com.github.opensharing.framework.springcloud.seata.at.controller;

import com.github.opensharing.framework.springcloud.seata.at.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 扣减账户余额
     *
     * @param userId 用户ID
     * @param amount 扣减金额
     * @param error  是否模拟错误（触发回滚）
     */
    @PostMapping("/debit")
    public ResponseEntity<Map<String, Object>> debit(
            @RequestParam("userId") String userId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam(value = "error", required = false, defaultValue = "false") boolean error) {

        log.info("接收扣款请求 - userId: {}, amount: {}, error: {}", userId, amount, error);

        Map<String, Object> result = new HashMap<>();

        try {
            // error 参数直接透传到 Service 层；
            // Service 在「扣减 SQL 执行成功 + undo_log 写入 + 分支已注册」之后再抛异常，
            // 这样才能真正验证 Seata AT 模式根据 undo_log 反向补偿的全局回滚能力。
            accountService.debit(userId, amount, error);

            result.put("success", true);
            result.put("message", "扣款成功");
            result.put("userId", userId);
            result.put("amount", amount);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("扣款失败: {}", e.getMessage(), e);

            result.put("success", false);
            result.put("message", "扣款失败: " + e.getMessage());
            result.put("userId", userId);
            result.put("amount", amount);

            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * 查询账户余额（Query Param 风格，便于浏览器和 curl 使用）
     *
     * @param userId 用户ID
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam("userId") String userId) {
        return doGetAccount(userId);
    }

    /**
     * 查询账户信息（RESTful Path 风格）
     *
     * @param userId 用户ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getAccount(@PathVariable("userId") String userId) {
        return doGetAccount(userId);
    }

    /**
     * 统一查询实现
     */
    private ResponseEntity<Map<String, Object>> doGetAccount(String userId) {
        log.info("查询账户 - userId: {}", userId);

        Map<String, Object> result = new HashMap<>();

        try {
            var account = accountService.getAccount(userId);
            if (account != null) {
                result.put("success", true);
                result.put("data", account);
            } else {
                result.put("success", false);
                result.put("message", "账户不存在");
            }
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("查询失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
