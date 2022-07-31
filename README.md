# 黏液科技進度

 一個 slimefun 插件，可讓你完成和配置進度

> 此為**非官方**版本, 請勿在該作者問題追蹤內回報! <br>
> [原作連結](https://github.com/qwertyuioplkjhgfd/SlimefunAdvancements) | [非官方Discord](https://discord.gg/GF4CwjFXT9)

| 非官方繁體中文版 | 官方英文版 |
| -------- | -------- |
| 點下方圖片下載 | 點下方圖片下載 |
| [![Build Status](https://xmikux.github.io/builds/SlimeTraditionalTranslation/SlimefunAdvancements/main/badge.svg)](https://xmikux.github.io/builds/SlimeTraditionalTranslation/SlimefunAdvancements/main) | [![Build Status](https://thebusybiscuit.github.io/builds/qwertyuioplkjhgfd/SlimefunAdvancements/main/badge.svg)](https://thebusybiscuit.github.io/builds/qwertyuioplkjhgfd/SlimefunAdvancements/main) | 

## 配置

配置文件可以在 `plugins/SFAdvancements/` 文件夾中找到

### groups.yml

yml 中的每一項代表一個進度組, 其中 key 是組的 key.<br>
key 用於引用 `advancements.yml` 中的組.<br>
每個組都有一個 `display`, 它是一個物品. 它應該是一個物品的代表.<br>
該物品用於在 GUI 中組別顯示圖標.<br>
你可以選擇為組指定一個 `background` 字串, 它在原版 GUI 中使用. (預設情況下, 組將會使用基岩做為材質)<br>
它應該是材質文件名稱. 這些文件名可以在 https://mcasset.cloud/ 的 `assets/minecraft/textures/block/` 中找到指定版本.

### 物品代表

您可以通過幾種方式表示一個物品.<br>
1. 只是字串, 物品的 id ([原版材料](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) 或 [slimefun 物品 id](https://sf-items.walshy.dev/))
2. 帶有字串 `type` (物品 id), 可選字串 `name`, 和可選字串列表 `lore` 的對象.
3. 物品的序列化表示

您可以通過在遊戲中將物品握在手中並輸入 `/sfa dumpitem` 來生成物品的資料.<br>
結果將顯示在控制台中.

在 `groups.yml` 的 #1 例子
```yaml
my_cool_group:
  display: NETHER_STAR
  background: glass

my_other_group:
  display: ELECTRIC_MOTOR
  background: redstone_block
```

在 `groups.yml` 的 #2 例子
```yaml
basic:
  display:
    type: SLIME_BALL
    name: "&f基礎"
    lore:
      - "&7&o黏液的核心精神."

electric:
  display:
    type: REDSTONE
    name: "&e電力"
    lore:
      - "&7&o文明的中心."
```

在 `groups.yml` 的 #2 例子
```yaml
hi:
  display:
    ==: org.bukkit.inventory.ItemStack
    v: 2865
    type: IRON_INGOT
    meta:
      ==: ItemMeta
      meta-type: UNSPECIFIC
      display-name: '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"aqua","text":"鋅錠"}],"text":""}'
      PublicBukkitValues:
        slimefun:slimefun_item: ZINC_INGOT
```

### advancements.yml

這是你所有進度的地方.<br>
每個任何代表一個進度，其中 key 是進度的關鍵.<br>
(它存儲為 NamespacedKey `sfadvancements:<key>`)<br>
進度包含組(group)、父級(optional parent)、顯示(display)、名稱(name)、條件(criteria)和可選獎勵(optional rewards).<br>

組(group) 是 `groups.yml` 中定義的 id .

父級(parent) 是一個不同進度類別的 ID. (對於進度樹)

顯示(display) 是一個物品.

名稱(name) 是當完成進度時出現在聊天中的進度名字.

(顯示和名稱支持帶有 `&` 的顏色代碼)

條件是一個部分，其中每個物品是一個條件，關鍵是條件 key.

#### 條件

每個進度的條件 key 應該是唯一的 (但可以在不同的進度中共享密鑰).<br>
字串本身無關緊要，但如果沒有指定名稱，它將使用 key.<br>
每個條件都有一個名稱、類型和可選的其他參數.

該名稱是出現在 gui 中以表示進度的名稱. 

類型是條件的類型。默認情況下，這些是默認條件類型:
- `消耗(consume)`
  - 用於吃物品
  - 有一個物品參數 `item`, 這是要消耗的物品
  - 請注意，這僅適用於消耗原版物品, 而不適用於異國花園的水果
  - 使用 `interact` 是因為它有一個數量參數 `amount`, 即要消耗的物品數量
- `互動(interact)`
  - 用於右鍵點擊物品
  - 有一個物品參數 `item`, 表示右擊的物品
  - 有一個數量參數 `amount`, 表示交互的次數
- `物品欄(inventory)`
  - 在身上中有物品
  - 有一個物品參數 `item`, 表示物品欄裡的物品
  - 有一個數量參數 `amount`, 表示完成條件的物品的數量
- `多重方塊(multiblock)`
  - 與黏液多重方塊互動
  - 有一個字串參數 `multiblock`, 是黏液多重方塊的 id
- `放置(place)`
  - 放置方塊
  - 有一個物品參數 `item`, 表示要放置的物品
  - 有一個數量參數 `amount`, 表示要放置的物品的數量
  - 請注意，沒有針對玩家反復破壞方塊的保護措施，因此對於大多數物品，只需使用一個作為數量
- `研究(research)`
  - 完成研究
  - 有一個字串參數 `research`, 它是研究的命名空間 key
    - 命名空間 key 的格式為 "plugin:key", 因此對於黏液研究, 是 "slimefun:research"
      - 例如: "slimefun:ender_talismans"
- `擊殺生物(mobkill)`
  - 擊殺生物
  - 參數 'amount', 多少隻怪物需要殺死
  - 有一個字串參數 `entity` 表示要擊殺的生物
  - 實體類型通常是小寫, 用下劃線分隔 (例如. `stray`, `cave_spider`, `glow_squid` 等等.)
- `搜尋(search)`
  - 用於搜尋黏液科技指南中的一個字串
  - 字串參數為 `search`, 要搜尋確切的字串指定名稱

#### 獎勵

獎勵對不同類型有不同的部分.<br>
(目前，唯一的類型是 `commands`)<br>
名稱決定獎勵的類型.

獎勵類型:
- 指令
  - 是一個字串列表, 每一行都是要運行的指令
  - 可以通過 `%p%` 查看完成進度的玩家姓名

## 權限

`sfa.command.<command name>`: 允許用戶使用命令

## 自定義標準 (開發人員)

見 [api.md](https://github.com/qwertyuioplkjhgfd/SlimefunAdvancements/blob/main/api.md)

## 待做:
- ~~標準系統~~
  - ~~物品欄標準~~
  - 工藝標準 (很快，請參閱 [Slimefun/Slimefun4#3439](https://github.com/Slimefun/Slimefun4/pull/3439))
  - ~~互動標準~~
      - ~~放置標準~~
  - ~~研究標準~~
- ~~可配置性~~
- ~~獎勵~~
- 添加進度
- ~~權限~~
- ~~加載默認進度 (來自其他插件)~~
- 更好的 README 文件, .github, ~~構建頁面~~
- ~~進度樹~~
- 進度 api (瘋狂)
- 作弊選單
- 文檔
