package com.zy.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zy.customer.databinding.CustomerActivityMarkdownBinding
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.core.factory.StrongEmphasisSpanFactory
import io.noties.markwon.simple.ext.SimpleExtPlugin
import java.util.regex.Matcher
import java.util.regex.Pattern


class MarkdownActivity : AppCompatActivity(){

    private val binding by lazy { CustomerActivityMarkdownBinding.inflate(layoutInflater)}
    private var mMarkdown: Markwon? = null

//    private val text = "Beijing is the capital of China.Rich historical relics and modern urban features. The following isSome suggested tourist attractions: 1. * * Palace MuseumCourtyard * *: As one of the largest palace complexes in the world, it was the home of the emperors of the Ming and Qing Dynasties.The Royal Palace. 2. **Tiananmen Square * *: Located in the center of Beijing,One of the largest city squares in the world.3. * * YiGarden of Harmony * *: It is an imperial garden in the Qing Dynasty of China, known asA model of garden art.4. * * Great Wall * *: such as eightDaling, Mutianyu and other passages are one of the symbols of China.The saying \\\"the Great Wall is not a true man\\\".5. * * Temple of Heaven Park * *: It is the place where ancient emperors offered sacrifices to heaven for a good harvest. The architectural art and layout here areVery unique. 6. **Beijing Hutong Tour * *: Ride a bicycle or walk through the old Beijing Hutong, feelThe traditional lifestyle of Beijing. 7.* * 798 Art Zone * *: Formerly a factory area, it has been transformed into a display.A fashionable area for modern art works and cultural events.8. * * National Grand Theatre * *A masterpiece of modern art architecture, regularPut on all kinds of performances.In addition, Beijing also hasModern shopping centers, food districts and bustling nightlife are one.A city that combines ancient culture with modern life.\\n"
//    private val text = "In 2024,The film industry presents a colorful side, and here are some of the works that have attracted much attention: 1.* * The Determination to Run Away * * -* * Director * *: Yin Lichuan -* * Starring: Yongmei, Jiang Wu, Wu Qian -* * Type * *:Drama/Family -* * Introduction * *: This mainland Chinese film tells the story of a family facing a decision.It is full of conflicts between emotional entanglement and family.2. * * The Lisbon Maru Sinks * * -* * Director * *: Fang Li -* * Starring * *: Fang Li, Tony Banham, Lin Agen-* * Genre * *: Documentary -* * Introduction * *: This documentary shows the sinking of the Lisbon Maru.Through in-depth discussion and interviews, the audience was shown the full picture of this historical event. 3.* * Summer Whispers * * -* * Director * *: Colm Byrd -* * Starring * *: Katherine Clinch, Carey Crowley, Andrew BanNit -* * Genre * *: Drama -* * Introduction * *: This Irish film tells a story that takes place in summer.It is full of the delicate description of the characters'Hearts and the profound perception of simple life.4. * * Silent Life * * -* * Director * *: Uberto Pasolini -* * Starring * *: Eddie Matson, Joanne FloGat, Andrew Buchan -* * Genre * *: Drama -* * Synopsis * *: This British-Italian co-production delves into the protagonist as he faces life's vicissitudes.His inner world, and how he finds the meaning of life. 5.* * Robot Dreams * * -* * Director * *: Pablo Berger -* * Starring * *: Ivan Labanda, Albert Trevor SyGallas, Rafa Calvo -* * Genre * *: Drama/Animation/Music -* * Introduction * *: This Spanish-French collaboration combines animation and music perfectly.It tells stories about dreams, love and technology.Generally speaking, these works cover different themes and styles.From family plots to historical documentaries, and then to profound humanistic care.Each of them reflects the diversity and depth of film art"
//    private val text = "2025-01-06 17:55:41.D  adapterBold result:最近上映的电影中，？**唐探1900**是其中的一部影片。这部电影作为《唐人街探案》系列电影的前传，带领观众回到1900年的旧金山，探索一起发生在唐人街的凶案。如需了解更多关于最近上映的电影信息，建议您关注电影院官网或影视资讯平台，以获取最新放映时间\n"
    private val text = "2025-01-06 17:55:41.265 2724-2724  adapterBold text:最近上映的电影中，**《唐探1900》** 是其中的一部影片。这部电影作为《唐人街探案》系列电影的前传，带领观众回到1900年的旧金山，探索一起发生在唐人街的凶案。如需了解更多关于最近上映的电影信息，建议您关注电影院官网或影视资讯平台，以获取最新放映"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mMarkdown = Markwon.builder(this)
//            .usePlugin(object: AbstractMarkwonPlugin() {
//                override fun configure(registry: MarkwonPlugin.Registry) {
//                    registry.require(SimpleExtPlugin::class.java, object : MarkwonPlugin.Action<SimpleExtPlugin> {
//                        override fun apply(plugin: SimpleExtPlugin) {
//                            plugin.addExtension(1, '*', StrongEmphasisSpanFactory())
//                        }
//                    });
//                }
//            })
//            .usePlugin(SimpleExtPlugin.create {
//                it.addExtension(1, '*', StrongEmphasisSpanFactory())
//            })
            .build()

//        SpanUtil.findRegex(text, object : SpanUtil.CallBack{
//            override fun onFind(start: Int, end: Int, spannableString: SpannableString) {
//                spannableString.subSequence()
//            }
//        }, "* *")

        val pattern = Pattern.compile("\\*\\*(.*?)\\*\\*")
        val matcher: Matcher = pattern.matcher(text.replace("* *", "**"))
        val result = StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(result, "**" + matcher.group(1).trim() + "** ")
        }
        matcher.appendTail(result)

        mMarkdown!!.setMarkdown(binding.markdownTV, result.toString())
    }


}