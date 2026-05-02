## Macro
- Macro
  - Edit
  - Macros
- Start Macro Recording
- Action
  - 줄아래로복사
  - 위로이동
- Stop Macro Recording
  - Edit
  - Macros
  - Start Macro Recording

  - 이름설정
  - Duplicate Line Above

- Keymap
- macros
- `Duplicate Line Above` 설정한매크로이름
- 단축키설정
- Ctrl+Alt+↑




##
인텔리제이 기본 기능 중에는 안타깝게도 **'위로 복사(Duplicate Above)'**라는 이름의 독립된 기능이나 전용 단축키는 공식적으로 존재하지 않습니다. 

대부분의 IDE가 `Ctrl` + `D`를 누르면 아래로 줄을 생성하는 방식을 채택하고 있어, 인텔리제이도 그 흐름을 따르고 있기 때문인데요. 이를 해결하기 위한 가장 현실적인 방법 3가지를 정리해 드립니다.

---

### 1. 매크로(Macros) 설정 (가장 추천)
단축키 한 번으로 **"복사하고 위로 올리기"**를 한 것처럼 동작하게 만드는 방법입니다.

1. **Edit** > **Macros** > **Start Macro Recording**을 누릅니다.
2. 다음 키를 순서대로 입력합니다:
   * `Ctrl` + `D` (줄 아래로 복사)
   * `Alt` + `Shift` + `↑` (복사된 줄을 위로 이동)
3. 아래 상태표시줄의 빨간 버튼을 눌러 녹화를 중지하고, 이름을 **"Duplicate Line Above"**로 저장합니다.
4. **Settings** > **Keymap**에서 저장한 매크로 이름을 검색해 원하는 단축키를 할당합니다.

### 2. 'Move Caret' 옵션 활용
기능 자체는 아래로 복사되지만, 커서의 위치를 조절해 결과적으로 위로 복사된 것처럼 느끼게 할 수 있습니다. 하지만 이 역시 기본 단축키에는 '위로' 옵션이 따로 없으므로 **Keymap**에서 직접 커스터마이징해야 합니다.

### 3. 플러그인 설치 (`Extra Actions`)
기본 기능에 갈증을 느끼는 개발자들이 만든 **Extra Actions** 같은 플러그인을 설치하면, 인텔리제이 설정에 `Duplicate Line Above`라는 액션이 새롭게 추가됩니다. 플러그인을 설치한 뒤 Keymap에서 단축키를 지정해 사용하면 됩니다.



---

**팁:** 만약 단순히 줄 순서를 바꾸고 싶은 것이라면 복사할 필요 없이 `Alt` + `Shift` + `↑` 또는 `↓` 키로 줄을 자유롭게 위아래로 이동시킬 수 있으니 이 기능도 함께 활용해 보세요.