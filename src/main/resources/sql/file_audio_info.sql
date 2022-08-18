/*
 Navicat Premium Data Transfer

 Source Server         : geodl_iecas
 Source Server Type    : PostgreSQL
 Source Server Version : 120003
 Source Host           : 192.168.9.64:32189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120003
 File Encoding         : 65001

 Date: 18/08/2022 16:08:01
*/

CREATE SEQUENCE file_audio_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99_999_999;

-- ----------------------------
-- Table structure for file_audio_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_audio_info";
CREATE TABLE "public"."file_audio_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_audio_info_id_seq'::regclass),
  "audio_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int4,
  "source" varchar(255) COLLATE "pg_catalog"."default",
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "is_public" bool,
  "create_time" timestamp(6),
  "keywords" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(50) COLLATE "pg_catalog"."default",
  "duration" int8,
  "file_length" int8,
  "batch_number" int4,
  "duration_str" varchar(255) COLLATE "pg_catalog"."default",
  "format" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."file_audio_info"."id" IS 'id';
COMMENT ON COLUMN "public"."file_audio_info"."audio_name" IS '影像名称';
COMMENT ON COLUMN "public"."file_audio_info"."path" IS '存放路径';
COMMENT ON COLUMN "public"."file_audio_info"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."file_audio_info"."source" IS '来源';
COMMENT ON COLUMN "public"."file_audio_info"."user_name" IS '用户名称';
COMMENT ON COLUMN "public"."file_audio_info"."keywords" IS '标签';
COMMENT ON COLUMN "public"."file_audio_info"."duration" IS '音频时长';
COMMENT ON COLUMN "public"."file_audio_info"."file_length" IS '文件长度';
COMMENT ON COLUMN "public"."file_audio_info"."batch_number" IS '上传批次';
COMMENT ON COLUMN "public"."file_audio_info"."duration_str" IS '字符串格式时长';
COMMENT ON COLUMN "public"."file_audio_info"."format" IS '格式';

-- ----------------------------
-- Primary Key structure for table file_audio_info
-- ----------------------------
ALTER TABLE "public"."file_audio_info" ADD CONSTRAINT "audio_pkey" PRIMARY KEY ("id");
