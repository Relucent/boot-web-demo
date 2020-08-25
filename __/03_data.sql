-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('0', '-1', '', 'admin', '{bcrypt}$2a$10$Eq1XFZbrSqFlFQmkePcMXO1wyVQGgkI6WgWFL7Uxqq3X4P2mXexqi', '管理员', '管理员', 1, 0, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('10000', '0', '系统管理', 1, '', '', '100', '/10000/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10100', '10000', '用户管理', 2, '/system/user/user-index.html', '', '100', '/10000/10100/', 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `permission` VALUES ('10200', '10000', '角色管理', 2, '/system/role/role-index.html', '', '100', '/10000/10200/', 0, 0, NULL, NULL, NULL, NULL);
