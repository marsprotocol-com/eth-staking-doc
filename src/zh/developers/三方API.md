# 三方API接口

![API.png](images/三方接入API.png)

<!-- TOC -->
* [三方API接口](#三方api接口)
  * [APIKey生成](#apikey生成)
    * [签名算法](#签名算法)
  * [限流控制](#限流控制)
  * [节点创建](#节点创建)
    * [申请deposit文件](#申请deposit文件)
    * [支付完成反馈](#支付完成反馈)
  * [节点退出](#节点退出)
    * [申请节点退出](#申请节点退出)
    * [查询节点退出状态](#查询节点退出状态)
  * [节点信息](#节点信息)
    * [查询单个节点](#查询单个节点)
    * [查询多个节点](#查询多个节点)
    * [查询所有节点(分页)](#查询所有节点分页)
  * [节点收益](#节点收益)
    * [withdraw收益](#withdraw收益)
    * [质押收益](#质押收益)
    * [mev收益](#mev收益)
<!-- TOC -->

## APIKey生成

apikey生成是在平台生成的，不对外提供接口
可以把生成apikey所需要的信息提交过来

### 签名算法

拟定的一个，还需要修改，暂时可以不考虑
* apiId放入参数
* utc的时间ts戳放入参数， ts的有效期为1800s
* 混合所有需要的最外层参数，不包含sign，进行排序，用 `&` 拼接成所有key一个字符串,ts，apiId需要加入值，用`=`分割运算，然后再加入apiSecret
* 将上述拼接的字符串，用md5加密算法进行签名

1. 举例1, 实际有用参数如下
```json
{
  "num": 2,
  "rewardAddress": "0x725b5f77496182ffdf67df1fa84cbb5f6111c2b4",
  "other": {
    "aa": "123",
    "bb": "456"
  }
}
```
2. 加入签名参数，变为如下
```json
{
  "num": 2,
  "rewardAddress": "0x725b5f77496182ffdf67df1fa84cbb5f6111c2b4",
  "other": {
    "aa": "123",
    "bb": "456"
  },
  "apiId": "xxxx",
  "ts": 1682393064
}
```

3. 按照排序拼接所有参数
`apiId=xxx&num&other&rewardAddress&ts=1682393064&xxxxsecretKeyxxxxx`
4. 计算上述md5值为
`1c5900ae27ebb4fde7e4f40b54da974c`
5. 加上签名拼接参数
```json
{
  "num": 2,
  "rewardAddress": "0x725b5f77496182ffdf67df1fa84cbb5f6111c2b4",
  "other": {
    "aa": "123",
    "bb": "456"
  },
  "apiId": "xxxx",
  "ts": 1682393064,
  "sign": "1c5900ae27ebb4fde7e4f40b54da974c"
}
```

## 限流控制

针对接口限流

针对IP限流

IP白名单设置

## 节点创建

### 申请deposit文件

接口 `POST /v1/outward/node/init/create`

请求参数

```json
{
  "num": 2,
  "rewardAddress": "0x725b5f77496182ffdf67df1fa84cbb5f6111c2b4",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "deposits": [
      {
        "deposit": "",
        "pubKey": "0xa9f4ef8b278e9a72b42ae52b6287b8ea271f14457c63b5cce4da4dab4f02a984f20cd3402028a44107fb91606c092372"
      },
      {
        "deposit": "",
        "pubKey": "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"
      }
    ]
  }
}
```

### 支付完成反馈

* 支付完成入库生成质押记录，质押节点表
* 交易txId为可选参数，尽量传递过来

接口 `POST /v1/outward/node/init/paid`

请求参数

```json
{
  "txId": "0xa9f4ef8b278e9a72b42ae52b6287b8ea271f14457c63b5cce4da4dab4f02a984f20cd3402028",
  "payAddress": "0x725b5f77496182ffdf67df1fa84cbb5f6111c2b4",
  "pubKeys": [
    "0xa9f4ef8b278e9a72b42ae52b6287b8ea271f14457c63b5cce4da4dab4f02a984f20cd3402028a44107fb91606c092372",
    "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"
  ],
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "data": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da"
}
```

## 节点退出

### 申请节点退出

接口 `POST /v1/outward/node/exit/apply`

请求参数

```json
{
  "pubKey": "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "data": "",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da"
}
```

### 查询节点退出状态

接口 `POST /v1/outward/node/exit/status`

请求参数

```json
{
  "pubKey": "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pubKey": "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453",
    "exitStatus": 5,
    "exitStatusMsg": "WITHDRAW",
    "updateTime": 1682393064
  }  
}
```

## 节点信息

### 查询单个节点

接口 `POST /v1/outward/node/detail/one`

请求参数

```json
{
  "pubKey": "0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

返回数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pubKey": "公钥",
    "validatorIndex": 123456,
    "payAddress": "支付地址",
    "rewardAddress": "收益地址",
    "status": 4,
    "statusMsg": "ACTIVE",
    "balanceNum": 32.123456,
    "rewardNum": 0.765432,
    "rewardStakeNum": 0.665432,
    "rewardMevNum": 0.1,
    "weekRewardNum": 0.064532,
    "weekRewardStakeNum": 0.064532,
    "weekRewardMevNum": 0.0,
    "totalWithdrawals": 0.612345,
    "payAt": 1682393064,
    "activeAt": 1682393064,
    "exitingAt": 0,
    "exitedAt": 0,
    "withdrawAt": 0,
    "actualWithdrawAt": 0
  }
}
```


| 列                  | 说明        |
|--------------------|-----------|
| balanceNum         | 余额数量      |
| rewardNum          | 收益数量      |
| rewardStakeNum     | 质押收益数量    |
| rewardMevNum       | mev收益数量   |
| weekRewardNum      | 七天收益数量    |
| weekRewardStakeNum | 七天质押收益数量  |
| weekRewardMevNum   | 七天MEV收益数量 |
| totalWithdrawals   | 提取到收益钱包数量 |
| payAt              | 支付时间      |
| activeAt           | 激活时间      |
| exitingAt          | 退出中时间     |
| exitedAt           | 已退出时间     |
| withdrawAt         | 本金到账时间    |
| actualWithdrawAt   | 本金实际到账时间  |

### 查询多个节点

接口 `POST /v1/outward/node/detail/many`

请求参数

```json
{
  "pubKeys": ["0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"],
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

| 参数      | 说明       |
|---------|----------|
| pubKeys | 最大支持100个 |

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": [{
    "pubKey": "公钥",
    "validatorIndex": 123456,
    "payAddress": "支付地址",
    "rewardAddress": "收益地址",
    "status": 4,
    "statusMsg": "ACTIVE",
    "balanceNum": 32.123456,
    "rewardNum": 0.765432,
    "rewardStakeNum": 0.665432,
    "rewardMevNum": 0.1,
    "weekRewardNum": 0.064532,
    "weekRewardStakeNum": 0.064532,
    "weekRewardMevNum": 0.0,
    "totalWithdrawals": 0.612345,
    "payAt": 1682393064,
    "activeAt": 1682393064,
    "exitingAt": 0,
    "exitedAt": 0,
    "withdrawAt": 0,
    "actualWithdrawAt": 0
  }
  ]
}
```

| 列                  | 说明        |
|--------------------|-----------|
| balanceNum         | 余额数量      |
| rewardNum          | 收益数量      |
| rewardStakeNum     | 质押收益数量    |
| rewardMevNum       | mev收益数量   |
| weekRewardNum      | 七天收益数量    |
| weekRewardStakeNum | 七天质押收益数量  |
| weekRewardMevNum   | 七天MEV收益数量 |
| totalWithdrawals   | 提取到收益钱包数量 |
| payAt              | 支付时间      |
| activeAt           | 激活时间      |
| exitingAt          | 退出中时间     |
| exitedAt           | 已退出时间     |
| withdrawAt         | 本金到账时间    |
| actualWithdrawAt   | 本金实际到账时间  |

### 查询所有节点(分页)

接口 `POST /v1/outward/node/detail/all`

请求参数

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

| 参数       | 说明         |
|----------|------------|
| pageNum  | 当前页        |
| pageSize | 每页数量，最大100 |

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "totalCount": 95,
    "totalPage": 10,
    "data": [
      {
        "pubKey": "公钥",
        "validatorIndex": 123456,
        "payAddress": "支付地址",
        "rewardAddress": "收益地址",
        "status": 4,
        "statusMsg": "ACTIVE",
        "balanceNum": 32.123456,
        "rewardNum": 0.765432,
        "rewardStakeNum": 0.665432,
        "rewardMevNum": 0.1,
        "weekRewardNum": 0.064532,
        "weekRewardStakeNum": 0.064532,
        "weekRewardMevNum": 0.0,
        "totalWithdrawals": 0.612345,
        "payAt": 1682393064,
        "activeAt": 1682393064,
        "exitingAt": 0,
        "exitedAt": 0,
        "withdrawAt": 0,
        "actualWithdrawAt": 0
      }
    ]
  }
}
```

## 节点收益

### withdraw收益

打到钱包的收益
接口 `POST /v1/outward/node/reward/withdraw`

请求参数

```json
{
  "pubKeys": ["0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"],
  "startAt": 1682100000,
  "endAt": 1682393064,
  "pageNum": 1,
  "pageSize": 10,
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```


| 参数       | 说明               |
|----------|------------------|
| pubKeys  | 可选参数，没有就不传递      |
| startAt  | 可选参数，没有不限制       |
| endAt    | 可选参数，必须大于startAt |
| pageNum  | 当前页              |
| pageSize | 每页数量，最大100       |

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "totalCount": 995,
    "totalPage": 100,
    "data": [
      {
        "pubKey": "公钥",
        "validatorIndex": 123456,
        "rewardAddress": "收益地址",
        "block": 123456,
        "slot": 123456,
        "blockRoot": "",
        "withdrawalIndex": 123456,
        "amount": 0.123456,
        "status": 0,
        "createdAt": 1682393064
      }
    ]
  }
}
```

按照createdAt创建时间进行倒叙排列，理论上一天一条数据

| 列      | 说明             |
|--------|----------------|
| amount | 收益数量           |
| status | 0 默认值, 1 退款32个 |

### 质押收益

接口 `POST /v1/outward/node/reward/stake`

请求参数

```json
{
  "pubKeys": ["0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"],
  "startAt": 1682100000,
  "endAt": 1682393064,
  "pageNum": 1,
  "pageSize": 10,
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "totalCount": 95,
    "totalPage": 10,
    "data": [
      {
        "pubKey": "公钥",
        "validatorIndex": 123456,
        "amount": 0.123456,
        "day": 365,
        "startTs": 1682300000,
        "endTs": 1682393064,
        "startBalance": 32.0123,
        "endBalance": 32.0234,
        "createdAt": 1682393064
      }
    ]
  }
}
```

### mev收益

接口 `POST /v1/outward/node/reward/mev`

```json
{
  "pubKeys": ["0x8eafeea384b111f90b80a733d6c713022d54ec4a612d000383c1775b804fb603485656f790bb679c5f6e8ecf848b3453"],
  "startAt": 1682100000,
  "endAt": 1682393064,
  "pageNum": 1,
  "pageSize": 10,
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "947dc49f219d30199b48f0ecc98a9af9"
}
```

响应数据

```json
{
  "code": 200,
  "message": "",
  "apiId": "APIId",
  "ts": 1682393064,
  "sign": "ac7dc49f219d30199b48f0ecc98a15da",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "totalCount": 95,
    "totalPage": 10,
    "data": [
      {
        "pubKey": "公钥",
        "validatorIndex": 123456,
        "rewardAddress": "收益地址",
        "amount": 0.512345,
        "ts": 1682392154,
        "createdAt": 1682393064
      }
    ]
  }
}
```
