-- 1. 어드민 계정
INSERT IGNORE INTO users (email, pw, name, addr, phone, reg_date, sns_auth, cash)
VALUES ('root@root', '$2b$12$Vt2o4fB/Ti2sRkTJIL9qiufDh55gpQDezeA2UsHQYgv6vbjnx5/7C', '관리자', '서울시 강남구', '010-0000-0000', NOW(), false, 0);

-- 2. 판매자 계정
INSERT IGNORE INTO users (email, pw, name, addr, phone, reg_date, sns_auth, cash)
VALUES ('seller@seller', '$2b$12$KJPjJtAIhLOdMl/np7jiI.S6bo1NhTcydcHYT00GOl5guLS7EdYqS', '판매자', '서울시 강서구', '010-1111-1111', NOW(), false, 0);

-- 3. 일반 유저 계정 (3개)
INSERT IGNORE INTO users (email, pw, name, addr, phone, reg_date, sns_auth, cash)
VALUES ('alpha@domain.com', '$2b$12$7CJxAoS/wOuR9pusR7iaF.WzdTH8KesXe.tz639Dy0AC.cofOLneK', '알파', '부산시 해운대구', '010-2222-2222', NOW(), false, 0);

INSERT IGNORE INTO users (email, pw, name, addr, phone, reg_date, sns_auth, cash)
VALUES ('beta@domain.com', '$2b$12$T1wAS364mendxcM1QIEpdOWhwdxBiAt/zCG3rmaPpIgl3aaJwb3G.', '베타', '대구시 수성구', '010-3333-3333', NOW(), false, 0);

INSERT IGNORE INTO users (email, pw, name, addr, phone, reg_date, sns_auth, cash)
VALUES ('gamma@domain.com', '$2b$12$7vCedw1g.erFQXTEZH8YvOrstK/k.HERMMDbCBGFan2cMtLfWchhO', '감마', '광주시 서구', '010-4444-4444', NOW(), false, 0);

-- 2. SellerAuths (판매자 인증 정보)
INSERT IGNORE INTO seller_auths (auth, apply_date, sign_date, store_name, busi_num, user_pk)
VALUES (
    'Accepted',
    NOW(),
    NOW(),
    '판매자 매장',
    '9876543210',
    (SELECT id FROM users WHERE email = 'seller@seller')
);

-- 1. Sellers (Users 중 판매자 계정에 대응)
INSERT IGNORE INTO sellers (user_pk, is_banned)
VALUES (
    (SELECT id FROM users WHERE email = 'seller@seller'),
    false
);

-- 3. Stores (판매자 매장)
INSERT IGNORE INTO stores (store_name, busi_num, seller_pk)
VALUES (
    '판매자 매장',
    '9876543210',
    (SELECT s.id FROM sellers s JOIN users u ON s.user_pk = u.id WHERE u.email = 'seller@seller')
);

---- 4. Items (매장 아이템 예시, 한 개씩)
---- alpha@domain.com 계정의 첫 매장에 아이템 추가 (예시로 연동)
--INSERT IGNORE INTO items (title, category, price, content, store_pk)
--VALUES (
--    '알파 첫 상품',
--    'DEFAULT_CATEGORY', -- 실제 Enum 저장값으로 변경 필요
--    10000,
--    '알파가 판매하는 첫 번째 상품입니다.',
--    (SELECT st.id FROM stores st JOIN sellers se ON st.seller_pk = se.id JOIN users u ON se.user_pk = u.id WHERE u.email = 'seller@seller' LIMIT 1)
--);
--
--INSERT IGNORE INTO items (title, category, price, content, store_pk)
--VALUES (
--    '베타 첫 상품',
--    'DEFAULT_CATEGORY',
--    20000,
--    '베타가 판매하는 첫 번째 상품입니다.',
--    (SELECT st.id FROM stores st JOIN sellers se ON st.seller_pk = se.id JOIN users u ON se.user_pk = u.id WHERE u.email = 'seller@seller' LIMIT 1)
--);

--INSERT IGNORE INTO items (title, category, price, content, store_pk)
--VALUES (
--    '감마 첫 상품',
--    'DEFAULT_CATEGORY',
--    30000,
--    '감마가 판매하는 첫 번째 상품입니다.',
--    (SELECT st.id FROM stores st JOIN sellers se ON st.seller_pk = se.id JOIN users u ON se.user_pk = u.id WHERE u.email = 'seller@seller' LIMIT 1)
--);
--
--INSERT IGNORE INTO options (add_price, option_info, amount, item_pk)
--VALUES
--    (1000, '기본 색상', 10,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '알파 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    ),
--    (2000, '확장 보증', 5,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '알파 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    );
--
--INSERT IGNORE INTO options (add_price, option_info, amount, item_pk)
--VALUES
--    (1500, '추가 배터리', 8,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '베타 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    ),
--    (2500, '프리미엄 케이스', 12,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '베타 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    );
--
--INSERT IGNORE INTO options (add_price, option_info, amount, item_pk)
--VALUES
--    (1200, '색상 선택', 20,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '감마 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    ),
--    (2200, '추가 충전기', 15,
--     (SELECT i.id
--      FROM items i
--      JOIN stores s ON i.store_pk = s.id
--      WHERE i.title = '감마 첫 상품' AND s.id = (
--          SELECT st.id
--          FROM stores st
--          JOIN sellers se ON st.seller_pk = se.id
--          JOIN users u ON se.user_pk = u.id
--          WHERE u.email = 'seller@seller'
--          LIMIT 1)
--      LIMIT 1)
--    );

