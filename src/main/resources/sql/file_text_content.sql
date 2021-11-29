/*
 Navicat Premium Data Transfer

 Source Server         : 9.64
 Source Server Type    : PostgreSQL
 Source Server Version : 120003
 Source Host           : 192.168.9.64:32189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120003
 File Encoding         : 65001

 Date: 29/11/2021 15:20:53
*/

CREATE SEQUENCE file_text_content_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_text_content
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_text_content";
CREATE TABLE "public"."file_text_content" (
  "id" int4 NOT NULL DEFAULT nextval('file_text_content_id_seq'::regclass),
  "text_file_id" int4,
  "content" text COLLATE "pg_catalog"."default",
  "line_number" int4
)
;
COMMENT ON COLUMN "public"."file_text_content"."id" IS '主键';
COMMENT ON COLUMN "public"."file_text_content"."text_file_id" IS '文本文件id';
COMMENT ON COLUMN "public"."file_text_content"."content" IS '内容';
COMMENT ON COLUMN "public"."file_text_content"."line_number" IS '行号';

-- ----------------------------
-- Primary Key structure for table file_text_content
-- ----------------------------
ALTER TABLE "public"."file_text_content" ADD CONSTRAINT "file_text_content_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table file_text_content
-- ----------------------------
ALTER TABLE "public"."file_text_content" ADD CONSTRAINT "text_file_id" FOREIGN KEY ("text_file_id") REFERENCES "public"."file_text_info" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
