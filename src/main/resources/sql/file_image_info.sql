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

 Date: 29/11/2021 15:17:52
*/

CREATE SEQUENCE file_image_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_image_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_image_info";
CREATE TABLE "public"."file_image_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_image_info_id_seq'::regclass),
  "image_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" int4,
  "source" varchar(255) COLLATE "pg_catalog"."default",
  "thumb" text COLLATE "pg_catalog"."default",
  "width" int4 DEFAULT 0,
  "height" int4 DEFAULT 0,
  "projection" text COLLATE "pg_catalog"."default",
  "min_lon" float4 DEFAULT 0,
  "min_lat" float4 DEFAULT 0,
  "max_lon" float4 DEFAULT 0,
  "max_lat" float4 DEFAULT 0,
  "batch_number" int4,
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "is_public" bool,
  "bands" int2,
  "bit" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "keywords" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(50) COLLATE "pg_catalog"."default",
  "resolution" float4 DEFAULT 0,
  "min_projection_x" float4,
  "min_projection_y" float4,
  "max_projection_x" float4,
  "max_projection_y" float4,
  "coordinate_system_type" varchar(255) COLLATE "pg_catalog"."default",
  "file_length" int8,
  "sensor_type" varchar(255) COLLATE "pg_catalog"."default",
  "sensor_name" varchar(255) COLLATE "pg_catalog"."default",
  "delete" bool
)
;
COMMENT ON COLUMN "public"."file_image_info"."id" IS 'id';
COMMENT ON COLUMN "public"."file_image_info"."image_name" IS '影像名称';
COMMENT ON COLUMN "public"."file_image_info"."path" IS '存放路径';
COMMENT ON COLUMN "public"."file_image_info"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."file_image_info"."source" IS '来源';
COMMENT ON COLUMN "public"."file_image_info"."width" IS '影像宽度';
COMMENT ON COLUMN "public"."file_image_info"."height" IS '影像高度';
COMMENT ON COLUMN "public"."file_image_info"."projection" IS '投影信息';
COMMENT ON COLUMN "public"."file_image_info"."min_lon" IS '最小经度';
COMMENT ON COLUMN "public"."file_image_info"."min_lat" IS '最小纬度';
COMMENT ON COLUMN "public"."file_image_info"."max_lon" IS '最大经度';
COMMENT ON COLUMN "public"."file_image_info"."max_lat" IS '最大纬度';
COMMENT ON COLUMN "public"."file_image_info"."user_name" IS '用户名称';
COMMENT ON COLUMN "public"."file_image_info"."bands" IS '波段数';
COMMENT ON COLUMN "public"."file_image_info"."bit" IS '数据类型';
COMMENT ON COLUMN "public"."file_image_info"."keywords" IS '标签';
COMMENT ON COLUMN "public"."file_image_info"."resolution" IS '分辨率';
COMMENT ON COLUMN "public"."file_image_info"."file_length" IS '文件长度';
COMMENT ON COLUMN "public"."file_image_info"."delete" IS '是否删除字段';
