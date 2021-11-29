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

 Date: 29/11/2021 15:25:51
*/

CREATE SEQUENCE file_video_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_video_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_video_info";
CREATE TABLE "public"."file_video_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_video_info_id_seq'::regclass),
  "video_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
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
  "resolution" float4,
  "batch_number" int4,
  "format" varchar(255) COLLATE "pg_catalog"."default",
  "frame_rate" varchar(255) COLLATE "pg_catalog"."default",
  "width" int4,
  "height" int4,
  "duration_str" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."file_video_info"."id" IS 'id';
COMMENT ON COLUMN "public"."file_video_info"."video_name" IS '影像名称';
COMMENT ON COLUMN "public"."file_video_info"."path" IS '存放路径';
COMMENT ON COLUMN "public"."file_video_info"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."file_video_info"."source" IS '来源';
COMMENT ON COLUMN "public"."file_video_info"."user_name" IS '用户名称';
COMMENT ON COLUMN "public"."file_video_info"."keywords" IS '标签';
COMMENT ON COLUMN "public"."file_video_info"."duration" IS '视频时长';
COMMENT ON COLUMN "public"."file_video_info"."file_length" IS '文件长度';
COMMENT ON COLUMN "public"."file_video_info"."resolution" IS '分辨率
';
COMMENT ON COLUMN "public"."file_video_info"."batch_number" IS '上传批次';

-- ----------------------------
-- Primary Key structure for table file_video_info
-- ----------------------------
ALTER TABLE "public"."file_video_info" ADD CONSTRAINT "video_pkey" PRIMARY KEY ("id");
