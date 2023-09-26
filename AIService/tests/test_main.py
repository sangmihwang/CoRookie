from fastapi.testclient import TestClient

from ..app.main import app

client = TestClient(app)


def test_bart_item():
    response = client.post(
        "/items/",
        json={
            "text": "나는 어제 강남구 역삼동에 있는 빵집에서 가서 몽블랑, 판나코타 그리고 금귤 쨈을 사왔어! 특히 금귤쨈이 빵에 발라먹으면 그렇게 맛있더라.\n나도 그 빵집 다녀온 적이 있어 나는 무화과가 들어간 깜빠뉴가 나의 취향이었지.\n깜빠뉴도 꽤나 맛있는 메뉴이긴 해. 김칠중씨는 어떻게 빵 좋아하세요?\n아 저는 빵보다는 밥을 많이 먹어요. 우리나라는 비정상적으로 빵값이 비싸더라구요.\n저도 동의해요 밀가루 가격대비 빵 값이 너무 비싼데 부대비용이 그정도 값어치를 하는지 잘 모르겠어요\n그런 얘기는 집어치우고 우리 앞으로 진행할 프로젝트에 대해서 얘기해 봅시다.\n",
            "start_time": "start_time",
        },
    )
    assert response.status_code == 200
