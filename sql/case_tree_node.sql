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

 Date: 29/07/2021 11:30:13
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
INSERT INTO "public"."case_tree_node" VALUES (1, '系统配置', NULL, 't', 0, 53, NULL, 'f', NULL, '2021-07-16 15:28:40.491175', 'admin', NULL, 0, 'v1');
INSERT INTO "public"."case_tree_node" VALUES (1001, '前端组件', NULL, 't', 0, NULL, NULL, 'f', NULL, '2021-07-16 15:28:40.491175', NULL, NULL, 0, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (138, '配置属性onDropDownRetrieve={script}，
valueFieldInDropDown，onChange，正常使用', 'case-00177', 'f', 127, 137, NULL, 'f', 'irene.chang', '2021-07-29 11:12:45.342', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (127, '组件lw-select', NULL, 't', 0, NULL, NULL, 'f', 'irene.chang', '2021-07-29 11:12:45.296', NULL, NULL, 0, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (128, '配置属性dropDownQuery={queryName}，获取下拉内容时发送相应请求 ', 'case-00167', 'f', 127, NULL, 129, 'f', 'irene.chang', '2021-07-29 11:12:45.32', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (129, '配置属性onDropDownRetrieve={script}...获取下拉内容时执行该脚本，下拉内容正常显示', 'case-00168', 'f', 127, 128, 130, 'f', 'irene.chang', '2021-07-29 11:12:45.323', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (130, '配置属性dropDownageSizes=20或不配置，每页记录行数为20', 'case-00169', 'f', 127, 129, 131, 'f', 'irene.chang', '2021-07-29 11:12:45.325', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (131, '配置属性keywordFieldsInDropDown=
{字段1,字段2,字段3...}，下拉框支持搜索', 'case-00170', 'f', 127, 130, 132, 'f', 'irene.chang', '2021-07-29 11:12:45.328', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (132, '配置displayTemplate属性值，
获取到对应的显示内容模板', 'case-00171', 'f', 127, 131, 133, 'f', 'irene.chang', '2021-07-29 11:12:45.33', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (133, '配置属性valueFieldInDropDown多个key，
下拉框正常显示对应的值', 'case-00172', 'f', 127, 132, 134, 'f', 'irene.chang', '2021-07-29 11:12:45.333', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (134, '配置属性onChange={script}，
执行该脚本，可以选中下拉值/清空选中值', 'case-00173', 'f', 127, 133, 135, 'f', 'irene.chang', '2021-07-29 11:12:45.336', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (135, '配置dropDownRowTemplate属性值，
显示每行下拉内容模板', 'case-00174', 'f', 127, 134, 136, 'f', 'irene.chang', '2021-07-29 11:12:45.338', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (136, '配置dropDownHeaderTemplate属性值，
显示下拉内容头部模板', 'case-00175', 'f', 127, 135, 137, 'f', 'irene.chang', '2021-07-29 11:12:45.339', NULL, NULL, -1, 'v2');
INSERT INTO "public"."case_tree_node" VALUES (137, '
配置属性onDropDownRetrieve,valueFieldInDropDown,valueFieldInDropDown组合，正常使用

', 'case-00176', 'f', 127, 136, 138, 'f', 'irene.chang', '2021-07-29 11:12:45.341', NULL, NULL, -1, 'v2');
