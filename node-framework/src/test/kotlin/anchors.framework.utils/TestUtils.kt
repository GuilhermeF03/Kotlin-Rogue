package anchors.framework.utils

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import org.junit.jupiter.api.BeforeAll


fun headlessApp() {
    HeadlessApplication(
            object : ApplicationListener {
                override fun create() {}

                override fun resize(
                    width: Int,
                    height: Int,
                ) {}

                override fun render() {}

                override fun pause() {}

                override fun resume() {}

                override fun dispose() {}
            },
            HeadlessApplicationConfiguration(),
        )
    }
