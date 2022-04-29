package main.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Verdict {
  private String comment;
  private boolean is_confirmed;
  private String login;
  private String pageName;
}
