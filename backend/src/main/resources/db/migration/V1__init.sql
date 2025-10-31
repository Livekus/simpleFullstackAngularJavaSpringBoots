create table f_flag
(
    id          uuid primary key,
    key_slug    varchar(80)  not null unique,
    name        varchar(120) not null,
    description text,
    enabled     boolean      not null default false,
    tags        text[],
    owner       varchar(80),
    version     integer      not null default 0,
    created_at  timestamptz  not null default now(),
    created_by  varchar(80),
    updated_at  timestamptz  not null default now(),
    updated_by  varchar(80)
);

create index idx_flag_name on f_flag (name);
create index idx_flag_enabled on f_flag (enabled);
create index idx_flag_tags on f_flag using gin (tags);

create table audit_log
(
    id          bigserial primary key,
    entity_type varchar(64) not null,
    entity_id   varchar(64) not null,
    action      varchar(16) not null,
    actor       varchar(80) not null,
    at          timestamptz not null default now(),
    data        jsonb
);
create index audit_log_entity on audit_log (entity_type, entity_id);