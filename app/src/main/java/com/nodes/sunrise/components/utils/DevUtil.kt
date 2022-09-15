package com.nodes.sunrise.components.utils

import android.net.Uri
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry
import java.time.LocalDateTime

class DevUtil {
    companion object {
        fun createSampleChallenges(count: Int): ArrayList<Challenge> {
            val result = ArrayList<Challenge>()
            for (i in 1..count) {
                result.add(Challenge(0, String.format("도전 과제 %d", i)))
            }

            return result
        }

        fun createSampleEntries(): ArrayList<Entry> {
            val result = ArrayList<Entry>()

            val now = LocalDateTime.now()
            val sampleContent =
                "스며들어 긴지라 못할 생의 그들의 이것을 무엇을 황금시대다. 눈에 산야에 가진 아니다. 긴지라 뭇 그들의 가지에 무한한 힘차게 열매를 심장은 예수는 말이다. 주는 밝은 관현악이며, 얼음이 피가 그들의 풍부하게 용감하고 사막이다. 붙잡아 기관과 보이는 가는 생명을 그림자는 얼음이 무한한 사막이다. 바로 얼마나 우리의 무한한 낙원을 보내는 인류의 철환하였는가? 같으며, 그들은 천지는 천자만홍이 인생을 열매를 것이다.\n" +
                        "\n" +
                        "보배를 않는 수 인생에 부패뿐이다. 생생하며, 피고 인간이 않는 원질이 실로 것이다. 같이, 얼음에 같은 것이다. 풍부하게 옷을 하는 얼음 관현악이며, 위하여 찾아다녀도, 영원히 것이다. 예가 날카로우나 가지에 무엇을 오직 목숨이 얼음에 실로 있다."

            // 대표 샘플 엔트리 생성
            val samples = ArrayList<Entry>().apply {
                add(Entry(title = "사진 없는 엔트리"))
                add(Entry(title = "사진 1개 엔트리").apply {
                    photos = createPhotoList(1)
                })
                add(Entry(title = "사진 2개 엔트리").apply {
                    photos = createPhotoList(2)
                })
                add(Entry(title = "위치 정보 있는 엔트리", latitude = 37.532600, longitude = 127.024612))
                add(Entry(title = "위치 정보 없는 엔트리"))
                add(Entry(title = "타이틀 있는 엔트리", isTitleEnabled = true))

                // sample content 설정
                for (entry in this) {
                    entry.content = sampleContent
                }

                // 별개의 content를 필요로 하는 샘플 엔트리
                add(Entry(isTitleEnabled = false, content = "타이틀 없는 엔트리"))
            }

            val mutiplier = 5 // 샘플 중복 추가 개수

            // 샘플 엔트리를 결과 리스트에 추가
            for (i in 1..samples.size * mutiplier) {
                result.add(samples[i % samples.size].copy().apply { dateTime = now.minusDays(i.toLong()) })
            }

            return result
        }

        private fun createPhotoList(count: Int): List<Uri> {
            val testPhotoUri =
                Uri.parse("android.resource://com.nodes.sunrise/" + R.drawable.test_photo)
            val result = ArrayList<Uri>()

            for (i in 1..count) {
                result.add(testPhotoUri)
            }

            return result.toList()
        }
    }
}