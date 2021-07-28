/*
 Navicat Premium Data Transfer

 Source Server         : local pg
 Source Server Type    : PostgreSQL
 Source Server Version : 90506
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90506
 File Encoding         : 65001

 Date: 23/07/2021 17:53:50
*/


-- ----------------------------
-- Table structure for case_tree_node
-- ----------------------------
DROP TABLE IF EXISTS "public"."case_tree_node";
CREATE TABLE "public"."case_tree_node" (
  "id" int8,
  "label" varchar COLLATE "pg_catalog"."default",
  "case_id" varchar COLLATE "pg_catalog"."default",
  "is_dir" bool NOT NULL DEFAULT false,
  "parent_id" int8,
  "pre_id" int8,
  "post_id" int8,
  "is_delete" bool NOT NULL DEFAULT false,
  "create_user" varchar COLLATE "pg_catalog"."default",
  "create_date" timestamp(6) DEFAULT now(),
  "update_user" varchar COLLATE "pg_catalog"."default",
  "update_date" timestamp(6) DEFAULT now(),
  "status" int4,
  "is_v" varchar COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of case_tree_node
-- ----------------------------
INSERT INTO "public"."case_tree_node" VALUES (1001, '前端组件', NULL, 't', 0, NULL, NULL, 'f', NULL, '2021-07-16 15:28:40.491175', NULL, NULL, 0, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (1, '系统配置', NULL, 't', 0, NULL, NULL, 'f', NULL, '2021-07-16 15:28:40.491175', NULL, NULL, 0, 'v1');
