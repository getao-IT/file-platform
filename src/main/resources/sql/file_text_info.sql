/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.25
 Source Server Type    : PostgreSQL
 Source Server Version : 90515
 Source Host           : 192.168.30.25:31189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90515
 File Encoding         : 65001

 Date: 02/08/2021 10:42:53
*/

CREATE SEQUENCE file_text_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_text_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_text_info";
CREATE TABLE "public"."file_text_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_text_info_id_seq'::regclass),
  "text_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int4,
  "source" varchar(255) COLLATE "pg_catalog"."default",
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "is_public" bool,
  "create_time" timestamp(6),
  "keywords" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(50) COLLATE "pg_catalog"."default",
  "line_num" int8,
  "file_length" int8,
  "batch_number" int4
)
;
COMMENT ON COLUMN "public"."file_text_info"."id" IS 'id';
COMMENT ON COLUMN "public"."file_text_info"."text_name" IS '影像名称';
COMMENT ON COLUMN "public"."file_text_info"."path" IS '存放路径';
COMMENT ON COLUMN "public"."file_text_info"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."file_text_info"."source" IS '来源';
COMMENT ON COLUMN "public"."file_text_info"."user_name" IS '用户名称';
COMMENT ON COLUMN "public"."file_text_info"."keywords" IS '标签';
COMMENT ON COLUMN "public"."file_text_info"."line_num" IS '文件长度';
COMMENT ON COLUMN "public"."file_text_info"."file_length" IS '文件长度';
COMMENT ON COLUMN "public"."file_text_info"."batch_number" IS '上传批次';

-- ----------------------------
-- Primary Key structure for table file_text_info
-- ----------------------------
ALTER TABLE "public"."file_text_info" ADD CONSTRAINT "text_pkey" PRIMARY KEY ("id");
