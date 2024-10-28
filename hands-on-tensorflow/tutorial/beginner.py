#https://colab.research.google.com/github/tensorflow/docs/blob/master/site/en/tutorials/quickstart/beginner.ipynb?hl=ko

import numpy as np  # NumPy 라이브러리 임포트 (mnist 파일 로드용)
import tensorflow as tf  # TensorFlow 라이브러리 임포트

# 현재 TensorFlow 버전 출력
print("TensorFlow version:", tf.__version__)

# MNIST 데이터셋 로드 (이미 저장된 .npz 파일)
mnist = np.load('./mnist.npz')
# 훈련 데이터 및 레이블, 테스트 데이터 및 레이블 분리
x_train, y_train, x_test, y_test = mnist['x_train'], mnist['y_train'], mnist['x_test'], mnist['y_test']
# 이미지 데이터를 0과 1 사이로 정규화 (픽셀 값이 0~255에서 0~1로 변환)
x_train, x_test = x_train / 255.0, x_test / 255.0

# 신경망 모델 생성
model = tf.keras.models.Sequential([
    # 28x28 이미지를 1D 배열로 변환
    tf.keras.layers.Flatten(input_shape=(28, 28)),
    # 128개의 뉴런을 가진 Dense(완전 연결) 층, ReLU 활성화 함수 사용
    tf.keras.layers.Dense(128, activation='relu'),
    # 20%의 뉴런을 랜덤으로 생략하여 과적합 방지
    tf.keras.layers.Dropout(0.2),
    # 출력층, 10개의 뉴런 (각 숫자 클래스에 해당)
    tf.keras.layers.Dense(10)
])

# 모델을 사용하여 첫 번째 훈련 데이터에 대한 예측 수행
predictions = model(x_train[:1]).numpy()
predictions  # 예측 결과 확인

# 예측 결과에 softmax 적용 (확률로 변환)
tf.nn.softmax(predictions).numpy()

# 손실 함수 정의 (스파스 범주형 교차 엔트로피)
loss_fn = tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True)

# 첫 번째 훈련 데이터의 손실 계산
loss_fn(y_train[:1], predictions).numpy()

# 모델 컴파일 (최적화기, 손실 함수, 평가 지표 설정)
model.compile(optimizer='adam',
              loss=loss_fn,
              metrics=['accuracy'])

# 모델 훈련 (5번 학습)
model.fit(x_train, y_train, epochs=5)

# 테스트 데이터셋으로 모델 평가 (정확도 출력)
model.evaluate(x_test, y_test, verbose=2)

# 확률 모델 생성 (기존 모델에 softmax 층 추가)
probability_model = tf.keras.Sequential([
    model,
    tf.keras.layers.Softmax()  # 확률로 변환
])

# 테스트 데이터의 첫 5개 샘플에 대한 확률 예측
probability_model(x_test[:5])
