-- 데이터베이스 생성 (필요한 경우)
CREATE DATABASE IF NOT EXISTS payment_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE payment_db;

-- 기존 테이블 삭제 (개발 환경에서만)
-- DROP TABLE IF EXISTS payments;
-- DROP TABLE IF EXISTS orders;

-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      order_id VARCHAR(255) NOT NULL,
    order_name VARCHAR(255) NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_id (order_id),
    INDEX idx_orders_created_at (created_at),
    INDEX idx_orders_status (status),
    INDEX idx_orders_customer_email (customer_email),
    INDEX idx_orders_composite (status, created_at)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 결제 테이블
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        payment_key VARCHAR(255) NULL,
    order_id VARCHAR(255) NOT NULL,
    order_name VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'READY',
    method VARCHAR(50) NULL,
    requested_at VARCHAR(255) NULL,
    approved_at VARCHAR(255) NULL,
    receipt_url TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_payments_payment_key (payment_key),
    UNIQUE KEY uk_payments_order_id (order_id),
    INDEX idx_payments_created_at (created_at),
    INDEX idx_payments_status (status),
    INDEX idx_payments_customer_name (customer_name),
    INDEX idx_payments_composite (status, created_at),
    CONSTRAINT fk_payments_order_id
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
                                                   ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 초기 데이터 삽입 (테스트용 - 선택사항)
INSERT IGNORE INTO orders (order_id, order_name, total_amount, customer_name, customer_email, status)
VALUES
    ('order_sample_001', '테스트 상품', 50000.00, '김테스트', 'test@example.com', 'PENDING'),
    ('order_sample_002', '샘플 티셔츠', 25000.00, '이샘플', 'sample@example.com', 'COMPLETED');

-- 테이블 코멘트
ALTER TABLE orders COMMENT = '주문 정보 테이블';
ALTER TABLE payments COMMENT = '결제 정보 테이블';

-- 컬럼 코멘트
ALTER TABLE orders MODIFY COLUMN order_id VARCHAR(255) NOT NULL COMMENT '주문 고유 ID';
ALTER TABLE orders MODIFY COLUMN order_name VARCHAR(255) NOT NULL COMMENT '주문 상품명';
ALTER TABLE orders MODIFY COLUMN total_amount DECIMAL(19,2) NOT NULL COMMENT '총 주문 금액';
ALTER TABLE orders MODIFY COLUMN customer_name VARCHAR(100) NOT NULL COMMENT '고객명';
ALTER TABLE orders MODIFY COLUMN customer_email VARCHAR(255) NOT NULL COMMENT '고객 이메일';
ALTER TABLE orders MODIFY COLUMN status VARCHAR(50) NOT NULL COMMENT '주문 상태 (PENDING, COMPLETED, CANCELLED, FAILED)';

ALTER TABLE payments MODIFY COLUMN payment_key VARCHAR(255) NULL COMMENT '토스페이먼츠 결제 키';
ALTER TABLE payments MODIFY COLUMN order_id VARCHAR(255) NOT NULL COMMENT '주문 ID (FK)';
ALTER TABLE payments MODIFY COLUMN order_name VARCHAR(255) NOT NULL COMMENT '주문 상품명';
ALTER TABLE payments MODIFY COLUMN amount DECIMAL(19,2) NOT NULL COMMENT '결제 금액';
ALTER TABLE payments MODIFY COLUMN customer_name VARCHAR(100) NOT NULL COMMENT '고객명';
ALTER TABLE payments MODIFY COLUMN status VARCHAR(50) NOT NULL COMMENT '결제 상태';
ALTER TABLE payments MODIFY COLUMN method VARCHAR(50) NULL COMMENT '결제 수단';
ALTER TABLE payments MODIFY COLUMN requested_at VARCHAR(255) NULL COMMENT '결제 요청 시간';
ALTER TABLE payments MODIFY COLUMN approved_at VARCHAR(255) NULL COMMENT '결제 승인 시간';
ALTER TABLE payments MODIFY COLUMN receipt_url TEXT NULL COMMENT '영수증 URL';

-- 뷰 생성 (주문과 결제 정보 조인)
CREATE OR REPLACE VIEW v_order_payment AS
SELECT
    o.id as order_table_id,
    o.order_id,
    o.order_name,
    o.total_amount,
    o.customer_name,
    o.customer_email,
    o.status as order_status,
    o.created_at as order_created_at,
    o.updated_at as order_updated_at,
    p.id as payment_table_id,
    p.payment_key,
    p.amount as payment_amount,
    p.status as payment_status,
    p.method as payment_method,
    p.requested_at,
    p.approved_at,
    p.receipt_url,
    p.created_at as payment_created_at,
    p.updated_at as payment_updated_at
FROM orders o
         LEFT JOIN payments p ON o.order_id = p.order_id;

-- 통계를 위한 뷰
CREATE OR REPLACE VIEW v_payment_stats AS
SELECT
    DATE(created_at) as payment_date,
    status,
    method,
    COUNT(*) as count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount
FROM payments
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY DATE(created_at), status, method
ORDER BY payment_date DESC, status, method;

-- 프로시저 예제 (일별 결제 통계)
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS GetDailyPaymentStats(IN target_date DATE)
BEGIN
SELECT
    status,
    method,
    COUNT(*) as transaction_count,
    SUM(amount) as total_amount,
    AVG(amount) as average_amount,
    MIN(amount) as min_amount,
    MAX(amount) as max_amount
FROM payments
WHERE DATE(created_at) = target_date
GROUP BY status, method
ORDER BY status, method;
END //
DELIMITER ;

-- 결제 상태 변경 로그 테이블
CREATE TABLE IF NOT EXISTS payment_status_log (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  payment_id BIGINT NOT NULL,
                                                  old_status VARCHAR(50),
    new_status VARCHAR(50) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_payment_status_log_payment_id (payment_id),
    INDEX idx_payment_status_log_changed_at (changed_at)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 트리거 예제 (결제 상태 변경 로깅)
DELIMITER //
CREATE TRIGGER IF NOT EXISTS tr_payment_status_change
AFTER UPDATE ON payments
                            FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO payment_status_log (payment_id, old_status, new_status)
        VALUES (NEW.id, OLD.status, NEW.status);
END IF;
END //
DELIMITER ;