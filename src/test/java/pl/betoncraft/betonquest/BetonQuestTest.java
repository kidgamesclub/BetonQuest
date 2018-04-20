package pl.betoncraft.betonquest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BetonQuestTest {

  @Test
  public void testParseVariable_Full() {
    assertThat(BetonQuest.resolveVariables("%player.fullName%"))
          .containsExactlyInAnyOrder("player.fullName");

  }

  @Test
  public void testParseVariable_Multiple() {
    assertThat(BetonQuest.resolveVariables("Hey, %player.fullName% - Did you find %apples_count_3% apples?"))
          .containsExactlyInAnyOrder("player.fullName", "apples_count_3");
  }

  @Test
  public void ignoresLiquify_Multiple() {
    assertThat(BetonQuest.resolveVariables("Hey, {%red%}{{player.name}}{%endred%}Hey%blue% - Did you find " +
          "%apples_count_3% apples?"))
          .containsExactlyInAnyOrder("blue", "apples_count_3");
  }

  @Test
  public void ignoresLiquify_Beginning() {
    assertThat(BetonQuest.resolveVariables("{%red%}%blue%{%endred%}"))
          .containsExactlyInAnyOrder("blue");
  }

}
